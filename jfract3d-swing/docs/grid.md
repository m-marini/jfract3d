### GRID

Depth = 4

1
4 4
3 4 3
4 4 4 4
2 4 3 4 2
4 4 4 4 4 4
3 4 3 4 3 4 3
4 4 4 4 4 4 4 4
1 4 3 4 2 4 3 2 1

To install local jar into project maven repository run:

mvn deploy:deploy-file -Durl=file://C:/MMData/git/jfract3D/jfract3d-swing/repo/ -Dfile=libs/vec-math.jar -DgroupId=org.mmarini.javaed -DartifactId=vecmath -Dpackaging=jar -Dversion=1.5