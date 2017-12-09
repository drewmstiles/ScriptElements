#!/usr/bin/perl

use strict;
use warnings;
use Getopt::Long;
use Pod::Usage;
use File::Copy;
use Cwd;

my $LIB_NAME = 'script-elements';
my $VERSION_FILEPATH = './version.txt';
my $version = '';
my $dest_dir = '';
my $JAR_EXTENSION = '.jar';

GetOptions("version=s", \$version,
		   "dest=s", \$dest_dir);

if (!$dest_dir) {
	die "USAGE: $0 --dest directory";
}

# ===================
# === Subroutines ===
# ===================

sub get_yn {

	my ($prompt) = @_;

	print $prompt;
	while (1) {
		my $in = <STDIN>;
		chomp $in;
		if ($in =~ m/[YyNn]/) {
			return $in;
		}
		else {
			print "Please enter a [Y/n] character.";
		}
	}
}


# ===========
# === Run ===
# ===========

if ($version !~ /(\d+)\.(\d+)/) {
	$version = `cat $VERSION_FILEPATH`;
	chomp $version;
	printf "LOG: No version passed, using current %s\n", $version;
}

my $jar_file = $LIB_NAME . '-' . $version . $JAR_EXTENSION;
my $source_path = './release/' . $jar_file;

my $og_dir = getcwd();

chdir $dest_dir or die "ERROR: Couldn't access $dest_dir - $!";
my @prexisting_jars = glob($LIB_NAME . '-' . '*');
if (@prexisting_jars > 0) {
	my $prompt = sprintf("Found %d existing deployments of %s in %s, do you want to remove these? (Y/n): ", scalar @prexisting_jars, $LIB_NAME, $dest_dir);
	my $ui = get_yn($prompt);
	if ( lc $ui eq 'y') {
		foreach my $file (@prexisting_jars) {
			unlink $file;
		}
	}
	else {
		# nada
	}
}

chdir $og_dir;
			
if (copy($source_path, $dest_dir)) {
	printf "LOG: %s moved to %s\n", $jar_file, $dest_dir;
}
else {
	printf "ERROR: Couldn't move %s to %s\n", $source_path, $dest_dir;
	exit 1;
}
