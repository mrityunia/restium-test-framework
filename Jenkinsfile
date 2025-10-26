/*****************************************************
 * Jenkins Declarative Pipeline
 * Purpose: To build, test, and generate Cucumber reports
 * Agent: selenium-agent (ensure this label exists in Jenkins)
 *****************************************************/

pipeline {

    /*****************************************************
     * AGENT SECTION
     * Runs this pipeline on the Jenkins agent with label 'selenium-agent'.
     * Make sure you have an agent configured in Jenkins with this label.
     *****************************************************/
    agent { label 'selenium-agent' }

    /*****************************************************
     * STAGES SECTION
     * Each stage represents a logical step in your pipeline.
     *****************************************************/
    stages {

        /***********************
         * 1️⃣ CHECK AGENT INFO
         * Verifies that the correct Jenkins agent is being used.
         ***********************/
        stage('Check Agent') {
            steps {
                echo "Running on agent: ${env.NODE_NAME}"
            }
        }

        /***********************
         * 2️⃣ RUN SAMPLE COMMANDS
         * Basic Linux shell commands to confirm environment setup.
         ***********************/
        stage('Run Sample Commands') {
            steps {
                sh 'echo "Hello from agent!"'   // Print simple message
                sh 'uname -a'                   // Display OS details
                sh 'whoami'                     // Show user running Jenkins job
                sh 'pwd'                        // Show current working directory
            }
        }

        /***********************
         * 3️⃣ CLONE REPOSITORY
         * Clones your project from GitHub using SSH credentials.
         * Make sure 'git-ssh-key-agent' exists in Jenkins credentials.
         ***********************/
        stage('Clone Repository') {
            steps {
                git branch: 'main',
                    url: 'git@github.com:mrityunia/restium-test-framework.git',
                    credentialsId: 'git-ssh-key-agent'
            }
        }

        /***********************
         * 4️⃣ CHECK GIT FILES
         * Lists all files in the workspace after cloning.
         * Helps verify that the repo was checked out properly.
         ***********************/
        stage('Check git files') {
            steps {
                sh 'ls -la'
            }
        }

        /***********************
         * 5️⃣ RUN TEST CASES
         * Executes your Cucumber + TestNG tests using Maven.
         * The parameters:
         *   -Pparallel           → activates the parallel profile in your pom.xml
         *   -DthreadCount=4      → runs tests on 4 parallel threads
         *   -Dcucumber.filter.tags → filters which Cucumber tests to execute
         ***********************/
        stage('Run Test Cases') {
            steps {
                sh '''
                    mvn test \
                        -Pparallel \
                        -DthreadCount=4 \
                        -Dcucumber.filter.tags="(@ui and @google and @Sanity) or (@api and @posts)"
                '''
            }
        }

        /***********************
         * 6️⃣ GENERATE CUCUMBER REPORT
         * Uses Maven to create a JSON and enhanced HTML report.
         * Output will be stored under: target/cucumber-reports/
         ***********************/
        stage('Generate Cucumber Report') {
            steps {
                sh '''
                    mvn verify \
                        -Dcucumber.options="--plugin json:target/cucumber-reports/CucumberTestReport.json"
                '''
            }
        }
    }

    /*****************************************************
     * POST SECTION
     * Defines actions that will always run after all stages.
     *****************************************************/
    post {

        /***********************
         * ✅ ALWAYS BLOCK
         * Publishes the Cucumber JSON report in Jenkins UI.
         * The 'cucumber' step is provided by the Cucumber Reports plugin.
         ***********************/
        always {
            cucumber fileIncludePattern: 'target/cucumber-reports/**/*.json',
                     trendsLimit: 10,
                     sortingMethod: 'ALPHABETICAL'
        }

        /***********************
         * 🎉 SUCCESS BLOCK
         * Runs only when the build succeeds.
         ***********************/
        success {
            echo "✅ Tests passed and Cucumber report generated successfully!"
        }

        /***********************
         * ❌ FAILURE BLOCK
         * Runs only when the build fails.
         ***********************/
        failure {
            echo "❌ Some tests failed. Check the Cucumber HTML report for details."
        }
    }
}
