set Path=%PATH%;C:\android\sdkJ17\platform-tools
set GRADLE_HOME=C:\android\gradle-8.1.1\
set PATH=%PATH%;%GRADLE_HOME%\bin
gradle clean bundle --info
