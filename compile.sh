#!/bin/sh
{
	sed "s/^/\/\/ &/g" "$1"
	scala-cli "$1"
} | xsel -b
