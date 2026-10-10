module PotiPlayTrackReader
  # List tracks instead of using Fastlane 2.229's nil-unsafe track_version_codes.
  def self.version_codes(client:, package_name:, track:)
    edit_open = false
    client.begin_edit(package_name: package_name)
    edit_open = true

    Array(client.tracks(track)).flat_map do |play_track|
      Array(play_track.releases).flat_map do |release|
        Array(release.version_codes)
      end
    end.compact.map(&:to_i).uniq
  ensure
    client.abort_current_edit if edit_open
  end
end
