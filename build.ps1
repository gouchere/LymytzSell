# =============================================================================
# Script de construction de l'executable LymytzSell
# =============================================================================
# Prerequis : Maven, JDK 17, JavaFX SDK 17
# Adapter les chemins ci-dessous si necessaire
# =============================================================================

# --- Configuration -----------------------------------------------------------
$JDK_HOME      = "C:\Program Files\Eclipse Adoptium\jdk-17.0.13.11-hotspot"
$JAVAFX_SDK    = "D:\Logiciels\Javafx-SDK-17.0.13"

# Version lue automatiquement depuis pom.xml
$POM_XML       = "$PSScriptRoot\pom.xml"
$APP_VERSION   = ([xml](Get-Content $POM_XML)).project.version
if (-not $APP_VERSION) { Write-Error "Impossible de lire la version dans $POM_XML"; exit 1 }

$MAIN_JAR      = "lymytzSell-$APP_VERSION.jar"
$MAIN_CLASS    = "com.lymytz.lymytzsell.LymytzSellApplication"
$APP_NAME      = "Lymytz-sell"
$VENDOR        = "Lymytz"
$ICON          = "$PSScriptRoot\lymytz-sell.ico"
$RUNTIME_DIR   = "$PSScriptRoot\runtimejdk17"
$TARGET_DIR    = "$PSScriptRoot\target"
$MAVEN_VERSION = "3.9.9"
$MAVEN_DIR     = "$PSScriptRoot\.maven\apache-maven-$MAVEN_VERSION"
# -----------------------------------------------------------------------------

$ErrorActionPreference = "Stop"

function Write-Step {
    param([string]$Message)
    Write-Host ""
    Write-Host "============================================================" -ForegroundColor Cyan
    Write-Host "  $Message" -ForegroundColor Cyan
    Write-Host "============================================================" -ForegroundColor Cyan
}

function Get-Maven {
    # 1) Maven deja dans le PATH ?
    $mvn = Get-Command mvn -ErrorAction SilentlyContinue
    if ($mvn) { return $mvn.Source }

    # 2) Maven deja telecharge localement ?
    $localMvn = "$MAVEN_DIR\bin\mvn.cmd"
    if (Test-Path $localMvn) { return $localMvn }

    # 3) Telechargement automatique
    Write-Host "Maven introuvable - telechargement de Maven $MAVEN_VERSION..." -ForegroundColor Yellow
    $url    = "https://archive.apache.org/dist/maven/maven-3/$MAVEN_VERSION/binaries/apache-maven-$MAVEN_VERSION-bin.zip"
    $zip    = "$PSScriptRoot\.maven\maven.zip"
    $outDir = "$PSScriptRoot\.maven"

    New-Item -ItemType Directory -Force -Path $outDir | Out-Null
    [Net.ServicePointManager]::SecurityProtocol = [Net.SecurityProtocolType]::Tls12
    Invoke-WebRequest -Uri $url -OutFile $zip -UseBasicParsing
    Expand-Archive -Path $zip -DestinationPath $outDir -Force
    Remove-Item $zip

    if (-not (Test-Path $localMvn)) {
        throw "Echec du telechargement de Maven."
    }
    Write-Host "Maven telecharge : $localMvn" -ForegroundColor Green
    return $localMvn
}

# -----------------------------------------------------------------------------
# Verifications preliminaires
# -----------------------------------------------------------------------------
Write-Step "Verification de l'environnement"

if (-not (Test-Path $JDK_HOME)) {
    Write-Error "JDK introuvable : $JDK_HOME - Adapter la variable JDK_HOME dans ce script."
    exit 1
}
if (-not (Test-Path $JAVAFX_SDK)) {
    Write-Error "JavaFX SDK introuvable : $JAVAFX_SDK - Adapter la variable JAVAFX_SDK dans ce script."
    exit 1
}

$MAVEN_CMD = Get-Maven
Write-Host "Maven     : $MAVEN_CMD" -ForegroundColor Green
Write-Host "JDK       : $JDK_HOME" -ForegroundColor Green
Write-Host "JavaFX    : $JAVAFX_SDK" -ForegroundColor Green
Write-Host "Version   : $APP_VERSION (lu depuis pom.xml)" -ForegroundColor Green

# -----------------------------------------------------------------------------
# Etape 1 : Compilation Maven
# -----------------------------------------------------------------------------
Write-Step "Etape 1/3 - Compilation Maven (mvn clean package)"

Push-Location $PSScriptRoot
try {
    $env:JAVA_HOME = $JDK_HOME
    & $MAVEN_CMD clean package -DskipTests
    if ($LASTEXITCODE -ne 0) { throw "La compilation Maven a echoue (code $LASTEXITCODE)." }
} finally {
    Pop-Location
}

if (-not (Test-Path "$TARGET_DIR\$MAIN_JAR")) {
    Write-Error "Le fichier JAR attendu est introuvable : $TARGET_DIR\$MAIN_JAR"
    exit 1
}
Write-Host "JAR genere : $TARGET_DIR\$MAIN_JAR" -ForegroundColor Green

# -----------------------------------------------------------------------------
# Etape 2 : Construction du runtime avec jlink
# -----------------------------------------------------------------------------
Write-Step "Etape 2/3 - Construction du runtime Java (jlink)"

$JLINK  = "$JDK_HOME\bin\jlink.exe"
$JMODS  = "$JDK_HOME\jmods"
$FXLIB  = "$JAVAFX_SDK\lib"
$FXBIN  = "$JAVAFX_SDK\bin"

if (Test-Path $RUNTIME_DIR) {
    Write-Host "Suppression de l'ancien runtime : $RUNTIME_DIR" -ForegroundColor Yellow
    Remove-Item -Recurse -Force $RUNTIME_DIR
}

$MODULES = "java.base,java.desktop,java.management,java.logging,java.naming,java.sql,java.xml," +
           "javafx.base,javafx.controls,javafx.fxml,javafx.graphics,javafx.media,javafx.swing,javafx.web," +
           "jdk.unsupported,java.instrument,java.rmi,jdk.httpserver"

& $JLINK `
    --module-path "$JMODS;$FXLIB" `
    --add-modules $MODULES `
    --output $RUNTIME_DIR `
    --strip-debug `
    --no-man-pages `
    --no-header-files `
    --compress=2

if ($LASTEXITCODE -ne 0) { throw "jlink a echoue (code $LASTEXITCODE)." }

# Copie des DLLs JavaFX non prises en charge par jlink
Write-Host "Copie des DLLs JavaFX vers le runtime..." -ForegroundColor Yellow
if (Test-Path $FXBIN) {
    Copy-Item "$FXBIN\*.dll" "$RUNTIME_DIR\bin\" -Force
    Write-Host "DLLs copiees." -ForegroundColor Green
} else {
    Write-Warning "Dossier bin JavaFX introuvable ($FXBIN), DLLs non copiees."
}

# -----------------------------------------------------------------------------
# Etape 3 : Generation de l'installeur avec jpackage
# -----------------------------------------------------------------------------
Write-Step "Etape 3/3 - Generation de l'installeur (jpackage)"

$JPACKAGE = "$JDK_HOME\bin\jpackage.exe"

$JAVA_OPTIONS = (
    "--add-opens=javafx.graphics/com.sun.javafx.application=ALL-UNNAMED",
    "--add-opens=java.base/java.lang=ALL-UNNAMED",
    "--add-opens=javafx.controls/com.sun.javafx.scene.control.skin.resources=ALL-UNNAMED",
    "--add-exports=javafx.base/com.sun.javafx.event=ALL-UNNAMED",
    "--add-exports=javafx.controls/com.sun.javafx.scene.control.behavior=ALL-UNNAMED",
    "--add-exports=javafx.controls/com.sun.javafx.scene.control=ALL-UNNAMED",
    "--add-exports=javafx.graphics/com.sun.javafx.print=ALL-UNNAMED",
    "-Dflyway.enabled=false"
) -join " "

$iconArg = @()
if (Test-Path $ICON) {
    $iconArg = @("--icon", $ICON)
} else {
    Write-Warning "Icone introuvable ($ICON), l'installeur sera genere sans icone personnalisee."
}

& $JPACKAGE `
    --input $TARGET_DIR `
    --name $APP_NAME `
    --main-jar $MAIN_JAR `
    --main-class $MAIN_CLASS `
    --type msi `
    --runtime-image $RUNTIME_DIR `
    --vendor $VENDOR `
    --app-version $APP_VERSION `
    @iconArg `
    --java-options $JAVA_OPTIONS `
    --win-shortcut `
    --win-menu `
    --dest "$PSScriptRoot\installer"

if ($LASTEXITCODE -ne 0) { throw "jpackage a echoue (code $LASTEXITCODE)." }

# Renommage de l'installeur pour inclure la version explicitement
$generatedMsi = "$PSScriptRoot\installer\$APP_NAME-$APP_VERSION.msi"
$targetMsi    = "$PSScriptRoot\installer\$APP_NAME-$APP_VERSION.msi"

# jpackage génère déjà le nom avec la version (ex: Lymytz-sell-3.0.5.msi)
# On s'assure juste que le fichier existe bien
if (-not (Test-Path $generatedMsi)) {
    # Parfois jpackage génère sans le tiret — on cherche le MSI produit
    $foundMsi = Get-ChildItem "$PSScriptRoot\installer\*.msi" | Sort-Object LastWriteTime -Descending | Select-Object -First 1
    if ($foundMsi) {
        $targetMsi = "$PSScriptRoot\installer\$APP_NAME-$APP_VERSION.msi"
        if ($foundMsi.FullName -ne $targetMsi) {
            Rename-Item -Path $foundMsi.FullName -NewName "$APP_NAME-$APP_VERSION.msi" -Force
            Write-Host "Installeur renomme : $APP_NAME-$APP_VERSION.msi" -ForegroundColor Green
        }
    }
}

# -----------------------------------------------------------------------------
Write-Host ""
Write-Host "============================================================" -ForegroundColor Green
Write-Host "  BUILD TERMINE AVEC SUCCES !" -ForegroundColor Green
Write-Host "  Version   : $APP_VERSION" -ForegroundColor Green
Write-Host "  Installeur: $APP_NAME-$APP_VERSION.msi" -ForegroundColor Green
Write-Host "  Dossier   : $PSScriptRoot\installer" -ForegroundColor Green
Write-Host "============================================================" -ForegroundColor Green

