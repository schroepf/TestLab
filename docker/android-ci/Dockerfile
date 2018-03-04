FROM openjdk:8-jdk

MAINTAINER Tobias Schröpf <schroepf@gmail.com>

ARG ANDROID_USER="android"
ARG ANDROID_USER_HOME="/home/${ANDROID_USER}"

ARG ANDROID_COMPILE_SDK="27"
ARG ANDROID_SDK_TOOLS="3859397"
ARG ANDROID_BUILD_TOOLS="27.0.3"
ARG ANDROID_SDK_DIR="${ANDROID_USER_HOME}/android-sdk"

ENV ANDROID_HOME="${ANDROID_SDK_DIR}"
ENV PATH="${PATH}:${ANDROID_HOME}/tools:${ANDROID_HOME}/tools/bin"

RUN apt-get --quiet update --yes
RUN apt-get --quiet install --yes wget tar unzip lib32stdc++6 lib32z1 libqt5widgets5

RUN useradd -ms /bin/bash ${ANDROID_USER}
USER ${ANDROID_USER}
WORKDIR ${ANDROID_USER_HOME}

RUN wget --quiet https://dl.google.com/android/repository/sdk-tools-linux-${ANDROID_SDK_TOOLS}.zip
RUN unzip -q sdk-tools-linux-${ANDROID_SDK_TOOLS}.zip -d ${ANDROID_SDK_DIR}

RUN echo y | sdkmanager "platform-tools"
RUN echo y | sdkmanager "build-tools;${ANDROID_BUILD_TOOLS}"
RUN echo y | sdkmanager "platforms;android-${ANDROID_COMPILE_SDK}"
RUN echo y | sdkmanager "extras;android;m2repository"
RUN echo y | sdkmanager "extras;google;m2repository"
RUN echo y | sdkmanager "extras;m2repository;com;android;support;constraint;constraint-layout;1.0.2"
RUN echo y | sdkmanager "system-images;android-${ANDROID_COMPILE_SDK};google_apis_playstore;x86"

RUN avdmanager create avd -n pixel_xl -k "system-images;android-${ANDROID_COMPILE_SDK};google_apis_playstore;x86" --device "pixel_xl" --tag "google_apis_playstore"

CMD /bin/bash
