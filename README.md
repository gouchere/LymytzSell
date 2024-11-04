# LymytzSell
Application de gestion de caisse associé à Lymytz ERP
## Technologie:
- OpenJdk Eclipse Adoptium 17.0.13.11
  ![JDK](https://img.shields.io/badge/LymytzSell-2.0.1-orange)
  ![JDK](https://img.shields.io/badge/Jdk_Eclipse_Adoptium-17.0.13-orange)
  ![JDK](https://img.shields.io/badge/JavaFx_SDK-17.0.13-orange)
  ![JDK](https://img.shields.io/badge/PostgresSql-14-orange)

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
--app-version 2.0.1
--java-options "--add-opens=java.base/java.lang=ALL-UNNAMED --add-opens javafx.controls/com.sun.javafx.scene.control.skin.resources=ALL-UNNAMED --add-exports=javafx.base/com.sun.javafx.event=ALL-UNNAMED --add-exports=javafx.controls/com.sun.javafx.scene.control.behavior=ALL-UNNAMED --add-exports=javafx.controls/com.sun.javafx.scene.control=ALL-UNNAMED"                                                                                                           
```
#Version
V3
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