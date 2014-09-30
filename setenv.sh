#!/usr/bin/env bash

if $(test -z "$PS1") ; then
    echo "You must source this file not execute it"
    echo "run:"
    echo "    $ source setenv.sh"
    exit
fi

export BASEDIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd -P )"
export __PATH=$PATH

leave () {
    export PATH=$__PATH
    unset __PATH
    unset BASEDIR
    unset -f leave
    echo 'left the python virtual environment'
}

export PATH="$BASEDIR/build/install/pr03/bin/:$PATH"

echo "java environment activated"
echo "you can leave the java environment for this project with 'leave'"
