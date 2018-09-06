#!/usr/bin/env bash
# Get to the Travis build directory, configure git and clone the repo
WD=$(pwd)
SCALAVER=`cat build.sbt | grep "scalaVersion := " | cut -d\" -f2`
VER=`echo $SCALAVER | cut -d. -f1,2`
MSG=$TRAVIS_COMMIT_MESSAGE
TAG=`cat build.sbt | grep "val VERSION" | cut -d\" -f2`
MODULE=`cat build.sbt | grep "val module" | cut -d\" -f2`

git config --global user.email "travis@travis-ci.org"
git config --global user.name "travis-ci"

if [[ $TRAVIS_SCALA_VERSION == $SCALAVER ]]; then
    sbt -J-Xmx3g ++$TRAVIS_SCALA_VERSION coverageReport coverageAggregate
    bash <(curl -s https://codecov.io/bash)

    if [[ $TRAVIS_EVENT_TYPE == "push" ]]; then
        # GENERATE DOC
        sbt ++$SCALAVER unidoc

        # CLONE GH-PAGES
        cd $HOME
        git clone --quiet --branch=gh-pages git@github.com:koalanlp/wrapper-${MODULE} gh-pages > /dev/null

        # COPY & PUSH
        cd gh-pages
        git rm -rf ./api/* ./src/* ./project/*
        git rm -rf *.sbt *.sh *.md codecov.yml .gitmodules
        mkdir ./api

        mv $WD/target/scala-$VER/unidoc $HOME/gh-pages/api/scala
        mv $WD/target/javaunidoc $HOME/gh-pages/api/java
        mv $WD/README.md $HOME/gh-pages/index.md

        git add -f ./api
        git add -f ./index.md

        git commit -m "Lastest javadoc on successful travis build $TRAVIS_BUILD_NUMBER (RELEASE $TAG) auto-pushed to gh-pages"
        git push -fq origin gh-pages > /dev/null
    fi
fi