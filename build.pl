#!/usr/bin/perl

# === Modules ===

use strict;
use warnings;
use Getopt::Long;
use Pod::Usage;
use Cwd;
use My::Utils qw( validate_args script_basename );

# === Constants ===

my $VERSION_FILE = './version.txt';
my $SOURCES_FILE = './sources.txt';
my $CONFIG_FILE = './config.txt';
my $SOURCES_DIR = './src';
my $BINARIES_DIR = './bin';
my $CLASSPATH = './lib/*';
my $RELEASE_DIR = './release';
my $MAJOR_TYPE = 'major';
my $MINOR_TYPE = 'minor';
my $PATCH_TYPE = 'patch';

# === Subroutines ===

sub get_inc_type {

    validate_args(3, \@_);
    my ($is_major, $is_minor, $is_patch) = @_;

    if ( $is_major ) {
	return $MAJOR_TYPE;
    }
    elsif ( $is_minor ) {
	return $MINOR_TYPE;
    }
    else {
	return $PATCH_TYPE;
    }
}

sub increment_version {

    validate_args(1, \@_);
    my ($inc_type) = @_;

    my $major;
    my $minor;
    my $patch;

    die "ERROR: Could not find version file at $VERSION_FILE"
	unless -f $VERSION_FILE;

    my $old_version = `cat $VERSION_FILE`;
    chomp $old_version;
    if ($old_version =~ /(\d+)\.(\d+)\.(\d+)/) {
	$major = $1;
	$minor = $2;
	$patch = $3;
    }
    else {
	die "ERROR: No version could be parsed from $VERSION_FILE\n";
    }

    if ( $inc_type eq $MAJOR_TYPE ) {
	$major += 1;
    }
    elsif ( $inc_type eq $MINOR_TYPE ) {
	$minor += 1;
    }
    else {
	$patch += 1;
    }

    my $new_version = $major . '.' . $minor . '.' . $patch;

    printf "LOG: Incrementing %s to give version %s replacing %s\n",
	$inc_type, $new_version, $old_version;

    return $new_version;
}

sub build {

    validate_args(1, \@_);    
    my ($version) = @_;

    printf "LOG: Using sourcepath '%s'\n", $SOURCES_FILE;
    printf "LOG: Using classpath '%s'\n", $CLASSPATH;

    my @build_command = (
	'javac',
	'-sourcepath', $SOURCES_DIR,
	'-cp', $CLASSPATH,
	'-d', $BINARIES_DIR,
	"\@$SOURCES_FILE"
    );

    die "ERROR: Compilation failed"
	unless system(@build_command) == 0;

    printf "LOG: Build succeeded, writing %s to %s\n", $version, $VERSION_FILE;
    `echo $version > $VERSION_FILE`;
}

sub archive {

    validate_args(2, \@_);
    my ($lib, $version) = @_;

    my $jar_file = $lib . '-' . $version . '.jar';

    my $prev_dir = getcwd();
    
    chdir($BINARIES_DIR) or die "ERROR: Failed to enter $BINARIES_DIR";

    die "ERROR: Failed to create JAR"
	unless system("jar cvf $jar_file * >/dev/null 2>&1") == 0;

    chdir($prev_dir) or die "ERROR: Failed to return to $prev_dir";
    
    die "ERROR: Failed to ship JAR"
	unless rename("${BINARIES_DIR}/${jar_file}", "${RELEASE_DIR}/${jar_file}");
}

sub main {

    my $is_major;
    my $is_minor;

    GetOptions("is-major!", \$is_major,
	       "is-minor!", \$is_minor);

    my $is_patch = !$is_major && !$is_minor;

    die "ERROR: Could not find library config file at $CONFIG_FILE"
	unless -f $CONFIG_FILE;

    my $lib = `cat $CONFIG_FILE`;
    chomp $lib;
    
    my $inc_type = get_inc_type($is_major, $is_minor, $is_patch);
    my $version = increment_version($inc_type);
    
    build($version);

    archive($lib, $version);

    print "END: $lib-$version released\n";
}


# === Run ===

&main();
