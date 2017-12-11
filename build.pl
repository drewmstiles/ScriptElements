#!/usr/bin/perl

use strict;
use warnings;
use Getopt::Long;
use Pod::Usage;

my $VERSION_FILEPATH = './version.txt';
my $SOURCES_FILEPATH = './sources.txt';
my $SOURCES_DIR = './src';
my $BINARIES_DIR = './bin';
my $CLASSPATH = './lib/*';
my $JAR_PREFIX = 'script-elements-';
my $RELEASE_PATH = '../release/';

my $is_major;
my $is_minor;
my $cp = '';

GetOptions("is-major", \$is_major,
		   "is-minor", \$is_minor,
		   "cp=s", \$cp);

my $old_version = `cat $VERSION_FILEPATH`;
chomp $old_version;

my $major;
my $minor;

# Parse verioning
if ($old_version =~ /(\d+)\.(\d+)/) {
	$major = $1;
	$minor = $2;
}
else {
	die "No version could be parsed from %s\n", $VERSION_FILEPATH;
}

# Increment version
my $increment = '';
if ($is_major) {
	$increment = 'major';
	$major += 1;
}
else {
	$increment = 'minor';
	$minor += 1;
}

printf "LOG: Incrementing to %s version after %s\n", $increment, $old_version;

# Save version
my $new_version = $major . '.' . $minor;

# Build
my $classpath = $cp ? $cp . ':' . $CLASSPATH : $CLASSPATH;
printf "LOG: Building sources defined in %s\n", $SOURCES_FILEPATH;
printf "LOG: Using CLASSPATH '%s'\n", $classpath;
`javac -sourcepath $SOURCES_DIR \\
	-cp "$classpath" \\
	-d $BINARIES_DIR \\
	\@$SOURCES_FILEPATH`; # Must quote lib

if ($? != 0) {
	die "\nERROR: Compilation failed";
}

printf "LOG: Build succeeded, writing %s to %s \n", $new_version, $VERSION_FILEPATH;
printf "LOG: Writing %s to %s\n", $new_version, $VERSION_FILEPATH;
`echo $new_version > $VERSION_FILEPATH`;

printf "Compilation exit code: $?\n";
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
