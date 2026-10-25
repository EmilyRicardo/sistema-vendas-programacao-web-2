@echo off
setlocal EnableExtensions
set "MAVEN_VERSION=3.9.11"
set "MAVEN_HOME_LOCAL=%USERPROFILE%\.maven\apache-maven-%MAVEN_VERSION%"
if not exist "%MAVEN_HOME_LOCAL%\bin\mvn.cmd" (
  echo Maven nao foi encontrado. Baixando Apache Maven %MAVEN_VERSION% para o seu usuario...
  powershell -NoProfile -ExecutionPolicy Bypass -Command "$ErrorActionPreference='Stop'; $ProgressPreference='SilentlyContinue'; $root=Join-Path $env:USERPROFILE '.maven'; New-Item -ItemType Directory -Force -Path $root | Out-Null; $zip=Join-Path $env:TEMP 'apache-maven-%MAVEN_VERSION%-bin.zip'; Invoke-WebRequest -Uri 'https://archive.apache.org/dist/maven/maven-3/%MAVEN_VERSION%/binaries/apache-maven-%MAVEN_VERSION%-bin.zip' -OutFile $zip; Expand-Archive -Path $zip -DestinationPath $root -Force; Remove-Item $zip -Force"
  if errorlevel 1 (
    echo Nao foi possivel baixar Maven automaticamente. Confira a conexao com a internet ou instale Apache Maven.
    exit /b 1
  )
)
if "%~1"=="" (
  call "%MAVEN_HOME_LOCAL%\bin\mvn.cmd" clean package
) else (
  call "%MAVEN_HOME_LOCAL%\bin\mvn.cmd" %*
)
exit /b %ERRORLEVEL%
