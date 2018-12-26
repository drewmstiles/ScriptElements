#!/usr/bin/perl

use strict;
use warnings;
use Getopt::Long;
#use Pod::Usage;
#use File::Copy;
#use Cwd;
use My::Utils qw(ship_archive);

my $LIB = 'script-elements';
my $VERSION_FILEPATH = './version.txt';
my $DEFAULT_SHIP_PATH = '/usr/local/share/java';

sub main {

    my $version = '';
    my $dst_dir = '';

    GetOptions("version=s", \$version,
	       "dest=s", \$dst_dir);

    $dst_dir = $DEFAULT_SHIP_PATH unless $dst_dir;

    if ( $version !~ /(\d+)\.(\d+)/ ) {
	$version = `cat $VERSION_FILEPATH`;
	chomp $version;
	printf "LOG: No version passed, using current %s\n", $version;
    }

    ship_archive($LIB, $version, $dst_dir);
}

&main();
