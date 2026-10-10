require "fastlane"
require "supply"
require_relative "../lib/play_track_reader"

# Exercise the real Supply client without credentials or Google API requests.
class FakePublisherService
  attr_reader :deleted

  def initialize(releases)
    @releases = releases
    @deleted = false
  end

  def insert_edit(package_name)
    AndroidPublisher::AppEdit.new(id: "offline-edit")
  end

  def list_edit_tracks(package_name, edit_id)
    AndroidPublisher::TracksListResponse.new(tracks: [
      AndroidPublisher::Track.new(track: "internal", releases: [AndroidPublisher::TrackRelease.new(version_codes: ["99"])]),
      AndroidPublisher::Track.new(track: "production", releases: @releases),
    ])
  end

  def delete_edit(package_name, edit_id)
    @deleted = true
  end
end

[[nil, []], [[], []], [[AndroidPublisher::TrackRelease.new(version_codes: ["4"])], [4]]].each do |releases, expected|
  service = FakePublisherService.new(releases)
  client = Supply::Client.allocate
  client.client = service
  actual = PotiPlayTrackReader.version_codes(client: client, package_name: "com.poti.android", track: "production")
  raise "Expected #{expected.inspect}, got #{actual.inspect}" unless actual == expected
  raise "Read-only edit was not cleaned up" unless service.deleted && client.current_edit.nil?
end

puts "All 3 real Supply client integration tests passed (offline)."
