plugins {
    id("io.github.gradle-nexus.publish-plugin")
}
// Set up Sonatype repository

nexusPublishing {

    repositories {
        sonatype {
            stagingProfileId = System.getenv("SONATYPE_STAGING_PROFILE_ID")
            username = System.getenv("System.env.OSSR_USERNAME")
            password = System.getenv("System.env.OSSR_PASSWORD")
        }
    }
}
