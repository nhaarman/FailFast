# Releasing

 1. Update the `CHANGELOG.md` file with relevant info and date.
 2. Update version numbers:
  - `gradle.properties`;
  - `plugin/src/main/groovy/failfast/FailFastPlugin.groovy`;
  - `README.md`
 3. Commit: `git commit -am "Prepare version X.Y.Z."`
 4. Merge into `master`: `git checkout master`, `git merge dev`
 5. Tag: `git tag -a X.Y.Z -m "Version X.Y.Z"`
 6. Release: `gradlew clean build lint generateReleaseJavadoc assemble uploadArchives -PnexusUsername=xxx -PnexusPassword=xxx`
 7. Close and release on Sonatype
 8. Checkout `dev`: `git checkoutdev`
 9. Update version numbers to next "SNAPSHOT" version:
  - `gradle.properties`;
  - `plugin/src/main/groovy/failfast/FailFastPlugin.groovy`;
 10. Commit: `git commit -am "Prepare next development version."`
 11. Push: `git push && git push --tags`
 12. Update release information on https://github.com/nhaarman/FailFast/releases
 13. Grab a coffee.