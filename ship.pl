#!/usr/bin/perl

use strict;
use warnings;
use Getopt::Long;
use Pod::Usage;
use File::Copy;

my $VERSION_FILEPATH = './version.txt';

my $version = '';
my $dest_dir = '';

GetOptions("version=s", \$version,
		   "dest=s", \$dest_dir);

if (!($version && $dest_dir)) {
	die "USAGE: $0 --version version --dest directory";
}


# ===========
# === Run ===
# ===========

if ($version !~ /(\d+)\.(\d+)/) {
	die "No version could be parsed from %s\n", $VERSION_FILEPATH;
}

my $jar_file = 'script-elements-' . $version . '.jar';
my $source_path = './release/' . $jar_file;

if (copy($source_path, $dest_dir)) {
	printf "LOG: %s moved to %s\n", $jar_file, $dest_dir;
}
else {
	printf "ERROR: Couldn't move %s to %s\n", $source_path, $dest_dir;
	exit 1;
}
