# Spectra Specification Language
The synthesizer and its tools are developed over the Eclipse framework, therefore a working installation of Eclipse IDE for Java and DSL Developers (preferably version above or equals 2022-09) is a prerequisite.

This repository contains the Eclipse Xtext project for the Spectra language. In order to auto generate the language files from the xtext grammar:
- `git clone` the repository into your computer.
- Import the projects into an Eclipse workspace. After the clone, you should have a folder called `spectra-lang` in your file system, containing many Java projects. Import all these projects by 'Import -> Projects from Folder or Archive', then choosing `spectra-lang` directory, and selecting all the projects from there *except* the spectra-lang folder itself (it is not a real project and it might cause problems if imported together with the other real projects).
- Right-click on `src/tau.smlab.syntech/GenerateSpectra.mwe2` and `Run As -> MWE2 Workflow`.

For further information about the Spectra synthesizer and language, please visit `spectra-synt` repository.
