TODO:
- Add MwSt change field
- Print directly (with a script)

**To build** the artifacts you have to remove the */MF and */SF files from the jar with extracting.
The steps include:
- cd temp_jar
- jar xf ../TaxiRechnungen.jar
- rm META-INF/*.DSA META-INF/*.SF
- jar cmf META-INF/MANIFEST.MF ../cleaned_final_Taxirechnungen.jar

