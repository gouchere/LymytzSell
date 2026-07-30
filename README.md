# LymytzSell
Application de gestion de caisse associé à Lymytz ERP
## Technologie:
  ![JDK](https://img.shields.io/badge/LymytzSell-3.0.1-orange)
  ![JDK](https://img.shields.io/badge/Jdk_Eclipse_Adoptium-17.0.13-orange)
  ![JDK](https://img.shields.io/badge/JavaFx_SDK-17.0.13-orange)
  ![JDK](https://img.shields.io/badge/PostgresSql-14-orange)

## Exécuter l'application en developpement
### Préparer son environnement
- Installer JDK 17
- Installer JavaFx SDK 17
- Installer Jlink
- Installer Jpackage
- Installer JavaFX Scene Builder

### Chargement de la configuration
  L'application utilise un fichier de configuration nommé `application.properties` qui est en principe initialisé à la première exécution de l'application. 
  dans le dossier home de l'utilisateur "C:\Users\{user}\lymytz-sell\conf\application.properties".
Ce fichier contient des paramètres essentiels pour le fonctionnement de l'application, tels que les informations de connexion à la base de données, les paramètres de synchronisation, et d'autres configurations spécifiques à l'environnement d'exécution. Il est important de s'assurer que ce fichier est correctement configuré avant de lancer l'application pour éviter tout problème de connexion ou de fonctionnement.

## Génération d'un exécutable avec jpackage
### Contruction du runtime
```bash
jlink --module-path "C:\Progra~1\Eclipse Adoptium\jdk-17.0.13.11-hotspot\jmods;D:\Logiciels\Javafx-SDK-17.0.13\lib" \ 
--add-modules java.base,java.desktop,java.management,java.logging,java.naming,java.sql,java.xml,javafx.base,javafx.controls,javafx.fxml,javafx.graphics,javafx.media,javafx.swing,javafx.web,jdk.unsupported,java.instrument,java.rmi 
--output runtimejdk17
```
Copier ensuite les .dll qui ne sont pas prise en compte avec Jlink
```bash
cp D\:/Logiciels/Javafx-SDK-17.0.13/bin/*.dll ./runtimejdk17/bin/ 

```
Générer l'installeur
```bash
jpackage --input ./LymytzSell/target/
--name Lymytz-sell
--main-jar lymytzSell-1.0-SNAPSHOT.jar
--main-class com.lymytz.lymytzsell.LymytzSell
--type msi --runtime-image ./runtimejdk17
--app-version 3.0.3
--icon lymytz-sell.ico
--java-options "--add-opens=javafx.graphics/com.sun.javafx.application=ALL-UNNAMED --add-opens=java.base/java.lang=ALL-UNNAMED --add-opens javafx.controls/com.sun.javafx.scene.control.skin.resources=ALL-UNNAMED --add-exports=javafx.base/com.sun.javafx.event=ALL-UNNAMED --add-exports=javafx.controls/com.sun.javafx.scene.control.behavior=ALL-UNNAMED --add-exports=javafx.controls/com.sun.javafx.scene.control=ALL-UNNAMED --add-exports javafx.graphics/com.sun.javafx.print=ALL-UNNAMED"                                                                                                           
```
One Line 
``` bash
jpackage --input ./LymytzSell/target/ --name Lymytz-sell --main-jar lymytzSell-3.0.3.jar --main-class com.lymytz.lymytzsell.LymytzSell --type msi --runtime-image ./runtimejdk17 --vendor Lymytz --app-version 3.0.4 --icon logo_lymytzSell.ico --java-options "--add-opens=javafx.graphics/com.sun.javafx.application=ALL-UNNAMED --add-opens=java.base/java.lang=ALL-UNNAMED --add-opens javafx.controls/com.sun.javafx.scene.control.skin.resources=ALL-UNNAMED --add-exports=javafx.base/com.sun.javafx.event=ALL-UNNAMED --add-exports=javafx.controls/com.sun.javafx.scene.control.behavior=ALL-UNNAMED --add-exports=javafx.controls/com.sun.javafx.scene.control=ALL-UNNAMED --add-exports javafx.graphics/com.sun.javafx.print=ALL-UNNAMED" --win-shortcut --win-menu
```
## Lancer l'application en ligne de commande avec le runtime
```bash
./runtimejdk17/bin/java -jar --add-opens=java.base/java.lang=ALL-UNNAMED --add-opens javafx.controls/com.sun.javafx.scene.control.skin.resources=ALL-UNNAMED --add-exports=javafx.base/com.sun.javafx.event=ALL-UNNAMED --add-exports=javafx.controls/com.sun.javafx.scene.control.behavior=ALL-UNNAMED --add-exports=javafx.controls/com.sun.javafx.scene.control=ALL-UNNAMED ./LymytzSell/target/original-lymytzSell-1.0-SNAPSHOT.jar
```
#Version description
Version avec modèle de sychronisation sur un serveur distinct
#Librairies
    - Jersey 2.3
    - EclipseLink(JPA 2.1)
    - postgres jdbc 9.2
    - gson-2.8.6
    - javax.mail
    - jreport 5.6
    - json-20200518
    - controlsfx-8.40.18