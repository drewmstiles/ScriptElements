#!/usr/bin/perl

use strict;
use warnings;
use Getopt::Long;
use Pod::Usage;
use IO::Handle;

my $VERSION_FILEPATH = './version.txt';
my $SOURCES_FILEPATH = './sources.txt';
my $SOURCES_DIR = './src';
my $BINARIES_DIR = './bin';
my $LIBRARIES_FILEPATH = './lib/*';
my $JAR_PREFIX = 'script-elements-';
my $RELEASE_PATH = '../release/';

my $is_major = '';
my $is_minor = '';

GetOptions("is-major", \$is_major,
		   "is-minor", \$is_minor);

if (!($is_major || $is_minor)) {
	die "USAGE: $0 [--is-major | --is-minor]";
}

my $old_version = `cat $VERSION_FILEPATH`;

my $major;
my $minor;

# Parse verioning
if ($old_version =~ /(\d+)\.(\d+)/) {
	$major = $1;
	$minor = $2;
}
else {
	die "No version could be parse from %s\n", $VERSION_FILEPATH;
}

# Increment version
if ($is_major) {
	$major += 1;
}
elsif ($is_minor) {
	$minor += 1;
}
else {
	die "Version isn't changing, what're you doing here?";
}

# Save version
my $new_version = $major . '.' . $minor;
printf "LOG: Writing %s to %s\n", $new_version, $VERSION_FILEPATH;
`echo $new_version > $VERSION_FILEPATH`;

# Build
printf "LOG: Building sources defined in %s\n", $SOURCES_FILEPATH;
`javac -sourcepath $SOURCES_DIR \\
	-cp "$LIBRARIES_FILEPATH" \\
	-d $BINARIES_DIR \\
	\@$SOURCES_FILEPATH`; # Must quote lib path

chdir($BINARIES_DIR);

# Archive
my $jar_file = $JAR_PREFIX . $new_version . '.jar';
`jar cvf $jar_file *`;
if (rename($jar_file, $RELEASE_PATH . $jar_file)) {
	printf("LOG: %s released\n", $jar_file);
}
else {
	printf("ERROR: Failed to move %s to %s\n", $jar_file, $RELEASE_PATH);
}
