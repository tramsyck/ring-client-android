FROM gradle:jdk8

ENV LANG en_US.utf8
ENV LC_ALL en_US.utf8

RUN apt-get update && apt-get install -y --no-install-recommends \
	asciidoc \
	autogen \
	automake \
	autoconf \
	autopoint \
	gettext \
	ca-certificates \
	cmake \
	bc \
	bison \
	build-essential \
	bzip2 \
	doxygen \
	git \
	lib32stdc++6 \
	lib32z1 \
	libtool \
	locales \
	m4 \
	pkg-config \
	software-properties-common \
	ssh \
	unzip \
	wget \
	curl \
	yasm \
	nasm \
	zip \
	libpcre3 \
	libpcre3-dev \
	ruby ruby-dev \
	&& locale-gen $LANG $LC_ALL && update-locale $LANG $LC_ALL

# Android SDK tools
ENV ANDROID_HOME=/opt/android-sdk
ENV ANDROID_SDK_ROOT=${ANDROID_HOME}
RUN wget -O /tmp/android-tools.zip https://dl.google.com/android/repository/commandlinetools-linux-6609375_latest.zip && \
	mkdir -p ${ANDROID_HOME} && \
	unzip -q -d ${ANDROID_HOME} /tmp/android-tools.zip && \
	rm -f /tmp/android-tools.zip && \
	chown -R root:root ${ANDROID_HOME}
ENV PATH=${PATH}:${ANDROID_HOME}/tools/bin

# Swig 4.0.1
RUN wget -O /tmp/swig.tar.gz https://github.com/swig/swig/archive/rel-4.0.1.tar.gz && \
	tar xzf  /tmp/swig.tar.gz -C /opt && \
	cd /opt/swig-rel-4.0.1/ && ./autogen.sh && ./configure && make && make install && \
	cd .. && rm -rf /opt/swig-rel-4.0.1 /tmp/swig.tar.gz

# Android SDK libraries, NDK
RUN sdkmanager --sdk_root=${ANDROID_HOME} --update
RUN (while sleep 1; do echo "y"; done) | sdkmanager --sdk_root=${ANDROID_HOME} 'build-tools;30.0.2' \
	'platforms;android-30'\
	'extras;android;m2repository'\
	'extras;google;m2repository'\
	'ndk;21.3.6528147'
ENV ANDROID_SDK=${ANDROID_HOME}
ENV ANDROID_NDK=${ANDROID_HOME}/ndk/21.3.6528147

# Fastlane
RUN gem install fastlane -NV
ENV HOME=/tmp
