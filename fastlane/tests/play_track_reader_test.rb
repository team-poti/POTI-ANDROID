require_relative "../lib/play_track_reader"

Track = Struct.new(:releases)
Release = Struct.new(:version_codes)

class FakePlayClient
  attr_reader :aborted, :requested_track, :requested_package

  def initialize(tracks, begin_error: nil, read_error: nil)
    @tracks = tracks
    @begin_error = begin_error
    @read_error = read_error
    @aborted = false
  end

  def begin_edit(package_name:)
    raise @begin_error if @begin_error
    @requested_package = package_name
  end

  def tracks(track)
    @requested_track = track
    raise @read_error if @read_error
    @tracks
  end

  def abort_current_edit
    @aborted = true
  end
end

def assert_equal(expected, actual)
  raise "Expected #{expected.inspect}, got #{actual.inspect}" unless expected == actual
end

def read_codes(client)
  PotiPlayTrackReader.version_codes(client: client, package_name: "com.poti.android", track: "production")
end

cases = [
  ["missing tracks", nil, []],
  ["no production track", [], []],
  ["first release: nil releases", [Track.new(nil)], []],
  ["empty releases", [Track.new([])], []],
  ["existing release", [Track.new([Release.new(["4"])])], [4]],
  ["multiple releases and missing codes", [Track.new([Release.new(["3", "4"]), Release.new(nil), Release.new([nil, "4", "5"])])], [3, 4, 5]],
]

cases.each do |name, tracks, expected|
  client = FakePlayClient.new(tracks)
  assert_equal(expected, read_codes(client))
  assert_equal(true, client.aborted)
  assert_equal("production", client.requested_track)
  assert_equal("com.poti.android", client.requested_package)
  puts "PASS: #{name}"
end

[["authentication failure", :begin_error, false], ["API failure", :read_error, true]].each do |name, error_key, should_abort|
  error = RuntimeError.new(name)
  client = FakePlayClient.new([], **{ error_key => error })
  begin
    read_codes(client)
    raise "Expected #{name} to propagate"
  rescue RuntimeError => actual
    raise unless actual.equal?(error)
  end
  assert_equal(should_abort, client.aborted)
  puts "PASS: #{name} is not treated as an empty track"
end

puts "All 8 track reader tests passed."
