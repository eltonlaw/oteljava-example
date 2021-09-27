VERSION=7.2
wget https://services.gradle.org/distributions/gradle-${VERSION}-bin.zip -P /tmp
sudo unzip -d /opt/gradle /tmp/gradle-${VERSION}-bin.zip
sudo ln -s /opt/gradle/gradle-${VERSION} /opt/gradle/latest
echo 'export GRADLE_HOME=/opt/gradle/latest' > gradle.sh
echo 'export PATH=${PATH}:${GRADLE_HOME}/bin' > gradle.sh
chmod +x gradle.sh
source gradle.sh
sudo mv gradle.sh /etc/profile.d/gradle.sh
