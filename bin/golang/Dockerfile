FROM ubuntu:20.04@sha256:3093096ee188f8ff4531949b8f6115af4747ec1c58858c091c8cb4579c39cc4e

ENV DEBIAN_FRONTEND="noninteractive"

## core
RUN apt-get update && \
    apt-get install -y \
            sudo  \
            git-core  \
            rlwrap  \
            software-properties-common  \
            unzip wget curl net-tools lsof \
            build-essential

RUN sudo apt update && sudo apt install -y xvfb libgtk2.0-0 libxss1 libgconf-2-4

WORKDIR /tmp

## node
RUN curl -sL https://deb.nodesource.com/setup_12.x | bash - && \
    apt-get install -y nodejs 
RUN curl -sS https://dl.yarnpkg.com/debian/pubkey.gpg |  apt-key add - && \
    echo "deb https://dl.yarnpkg.com/debian/ stable main" |  tee /etc/apt/sources.list.d/yarn.list && \
    apt-get update && apt-get -y install yarn

## golang
# RUN curl -LO https://get.golang.org/$(uname)/go_installer && chmod +x go_installer && ./go_installer && rm go_installer
ENV PATH=$PATH:/usr/local/go/bin
ENV GO_DIST=go1.15.7.linux-amd64.tar.gz
RUN curl -LO https://golang.org/dl/$GO_DIST \
    && tar -C /usr/local -xzf $GO_DIST \
    && rm $GO_DIST \
    && go version

ARG workdir

WORKDIR ${workdir}