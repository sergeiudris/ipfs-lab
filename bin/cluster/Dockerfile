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
ENV GOPATH=/root/go
ENV PATH=$PATH:/usr/local/go/bin:${GOPATH}/bin
ENV GO_DIST=go1.15.7.linux-amd64.tar.gz
ENV GO111MODULE=on
RUN curl -LO https://golang.org/dl/${GO_DIST} \
    && tar -C /usr/local -xzf ${GO_DIST} \
    && rm ${GO_DIST} \
    && go version


## ipfs-cluster

# from https://github.com/ipfs/ipfs-cluster/blob/master/docker-compose.yml

ENV SUEXEC_VERSION v0.2
ENV TINI_VERSION v0.19.0
RUN set -eux; \
    dpkgArch="$(dpkg --print-architecture)"; \
    case "${dpkgArch##*-}" in \
        "amd64" | "armhf" | "arm64") tiniArch="tini-static-$dpkgArch" ;;\
        *) echo >&2 "unsupported architecture: ${dpkgArch}"; exit 1 ;; \
    esac; \
  cd /tmp \
  && git clone https://github.com/ncopa/su-exec.git \
  && cd su-exec \
  && git checkout -q $SUEXEC_VERSION \
  && make su-exec-static \
  && cd /tmp \
  && wget -q -O tini https://github.com/krallin/tini/releases/download/$TINI_VERSION/$tiniArch \
  && chmod +x tini

RUN mv /tmp/su-exec/su-exec-static /sbin/su-exec \
    &&  mv /tmp/tini /sbin/tini

RUN apt-get update && apt-get install -y ca-certificates

ENV IPFS_CLUSTER_PATH      /data/ipfs-cluster
ENV IPFS_CLUSTER_CONSENSUS crdt

RUN mkdir -p $IPFS_CLUSTER_PATH && \
    adduser --home $IPFS_CLUSTER_PATH --uid 1000 --ingroup users --disabled-login --disabled-password --gecos "" ipfs && \
    chown ipfs:users $IPFS_CLUSTER_PATH

EXPOSE 9094
EXPOSE 9095
EXPOSE 9096

ARG workdir

WORKDIR ${workdir}

