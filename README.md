# Android Security Framework

This project is the private part of the Android Security Toolkit - a Framework.
It should be published in Maven Central without sources and with correct
obfuscation (Proguard R8). Implementation of each Threat Detection should be
implemented in a private class, so that it is not exposed to public

## Project Architecture

Following diagram showcases current Architecture of the Android Security Toolkit

[TODO: Diagram]

This part of the Android Security Framework is private. It is published on the
maven central repository. The example app is published on the github, making
it possible for other users to contribute and use the framework.

## Release Process

In order to release a new version of this framework:

- increment the version of release
  in [build.gradle file](./securitytoolkit/build.gradle.kts)
- merge the changes via a release branch into main. Pipeline will make release

## DevOps

In order to support this project, the following tasks are present:

- Check the daily running pipeline for any errors and fix them
- Check user Feedback in the corresponding Example Project on GitHub
- Run the example App locally to check for any compilation or runtime errors
- Fix anything by contributing

## Contributing

In order to contribute to this repository:

- Read the whole README (this) file
- Setup local development environment
- Confirm to the branching model
- Create a feature PR and merge after a review

### Development Environment

## Coding Guidelines

We are following
the [Kotlin coding conventions](https://kotlinlang.org/docs/coding-conventions.html)
and the [Kotlin style guide](https://developer.android.com/kotlin/style-guide).

### Documentation

For further information on Documentation
see https://developer.android.com/kotlin/style-guide#documentation
and [Document Kotlin code: KDoc](https://kotlinlang.org/docs/kotlin-doc.html)

**DO**

+ ... place the opening `/**` on a separate line and begin each subsequent line
  with an asterisk
+ ... place short comments on a single line
+ ... put a space after `//`
+ ... put copyright and licence headers in a multiline comment

**PREFER**

+ ... incorporating the description of parameters and return values directly
  into the documentation comment
+ ... adding links to parameters wherever they are mentioned
+ ... using `@param` and `@return` only when a lengthy description is required
  which doesn't fit into the flow of the
  main text

**DON'T**

- ... put copyright and licence headers in KDoc-style or single-line-style
  comments

### Structure

A .kt file comprises the following, in order:

1. Copyright and/or license header (optional)
2. File-level annotations
3. Package statement
4. Import statements
5. Top-level declarations

The contents of a class should go in the following order:

1. Property declarations and initializer blocks
2. Secondary constructors
3. Method declarations
4. Companion object

**DO**

+ ... separate each section with one blank line
+ ... group import statements for classes, functions, and properties

**CONSIDER**

+ ... structuring declarations in a file after a logical order
+ ... ordering declarations for reading from top-to-bottom

**DON'T**

- ... use wildcard imports
- ... line-wrap imports

### Naming

**DO**

+ ... use lowerCamelCase for package names (`org.example.myProject`)
+ ... use UpperCamelCase for classes and objects
+ ... use SCREAMING_SNAKE_CASE for constant properties
+ ... use lowerCamelCase for top-level or object properties
+ ... use SCREAMING_SNAKE_CASE or UpperCamelCase for enum constants

**CONSIDER**

+ ... using a noun or a noun phrase explaining what a class
  is (`List`, `PersonReader`)
+ ... using a verb or a verb phrase saying what the method
  does (`close`, `readPersons`)
+ ... suggesting if a method is mutating an object (`sort`) or returning a new
  one (`sorted`)
+ ... capitalizing acronyms if they consist of two letters (`IOStream`) and
  capitalize only first letter if they are
  longer (`XmlFormatter`, `HttpInputStream`)

**AVOID**

- ... using meaningless words (`Manager`, `Wrapper`)

### Formatting

**DO**

+ ... use four spaces for indentation
+ ... put the opening brace at the end of the line where the construct begins
+ ... put the closing brace on a separate line aligned horizontally with the
  opening construct
+ ... put spaces around binary operators (`a + b`)
+ ... put spaces between control flow keywords (`if`, `when`, `for`,
  and `while`) and the corresponding opening
  parenthesis
+ ... put `get` and `set` keywords on separate lines for more complex properties
+ ... apply the Kernighan and Ritchie style on braces for nonempty blocks and
  block-like constructs
+ ... separate each statement with a line break

**PREFER**

+ ... using an expression body for functions with the body consisting of a
  single expression
+ ... omitting braces for `when` branches and `if` expressions which have no
  more than one `else` branch and which fit
  on a single line
+ ... omitting return types if an expression function body or a property
  initializer is a scalar value or the return
  type can be clearly inferred from the body

**CONSIDER**

+ ... one-line formatting for very simple read-only properties
+ ... using trailing commas

**AVOID**

- ... horizontal alignment of any kind
- ... lines longer than 80

**DON'T**

- ... use tabs for indentation
- ... put spaces around the "range to" operator (`0..i`)
- ... put spaces around unary operators (`a++`)
- ... put a space after `(`, `[`, or before `]`, `)`
- ... put a space around `.` or `?`
- ... put spaces around angle brackets used to specify type
  parameters: `class Map<K, V> { ... }`
- ... put spaces around `::`
- ... put a space before `?` used to mark a nullable type
- ... put a space before an opening parenthesis in a primary constructor
  declaration, method declaration or method call

### Idiomatic use of language features

**DO**

+ ... use named arguments when a method takes multiple parameters of the same
  primitive type
+ ... use named arguments for `Boolean`, unless the meaning of all parameters is
  absolutely clear from context
+ ... use the `..<` operator to loop over an open-ended range
+ ... use extension functions liberally

**PREFER**

+ ... declaring local variables and properties as `val` rather than `var` if
  they are not modified after initialization
+ ... using immutable collection interfaces (`Collection`, `List`, `Set`, `Map`)
  to declare collections which are not
  mutated
+ ... declaring functions with default parameter values to declaring overloaded
  functions
+ ... defining a type alias for functional types or a types with type parameters
  that are used multiple times in a
  codebase
+ ... `import ... as ...` if you use a private or internal type alias for
  avoiding name collision
+ ... using `if` for binary conditions instead of `when`
+ ... using higher-order functions (`filter`, `map` etc.) to loops
+ ... using a regular `for` loop instead `forEach`
+ ... using string templates to string concatenation

**CONSIDER**

+ ... restructuring a lambda so that it will have a single exit point
+ ... (if that's not possible or not clear enough) converting the lambda into an
  anonymous function

**AVOID**

- ... using multiple labeled returns in a lambda

**DON'T**

- ... use a labeled return for the last statement in a lambda

### Avoid redundant constructs

**AVOID**

- ... using semicolons
- ... returning Unit

**DON'T**

- ... use curly braces when inserting a simple variable into a string template

### Additional Coding guidelines

**DO**

+ ... use MVVM and repository pattern
+ ... handle state in the ViewModel
+ ... use Dependency Injection with Hilt like specified by Android (
  see https://developer.android.com/training/dependency-injection/hilt-android)
+ ... limit the scope of @Composable to describing UI components
+ ... follow the single-activity architecture pattern

## Linter

Different Linters are used to ensure the best code quality possible:

- gradle lint
- detekt

Gradle Lint uses standard configuration.
Detekt uses `config/detekt/detekt.yml` configuration file.

All of this are configured in the pipeline, but can also be configured locally
with the following methods:

- Intellij Plugins:
    - gradle lint can be run via "Code" -> "Inspect Code…" option
    - detekt plugin can be installed and configured
- Command Line:
    - run `./gradlew lint` from app root directory
    - run `./gradlew detekt` from app root directory

## Branching model

The branching workflow is based on Gitflow as
documented [here](https://www.atlassian.com/de/git/tutorials/comparing-workflows/gitflow-workflow).

<img src="https://wac-cdn.atlassian.com/dam/jcr:cc0b526e-adb7-4d45-874e-9bcea9898b4a/04%20Hotfix%20branches.svg?cdnVersion=431" alt="drawing" width="700" style="margin-bottom: 20px"/>

According to Gitflow two branches are representing the state of the current
project. The first one is the `main` branch,
which contains the release history and the `develop` branch, which is the
integration branch for new features. Every
feature is developed on its own branch, which is branched off from `develop`.
The name of a feature branch is
constructed according to the following scheme:

`feature/[Ticket number from Jira]-short-description-of-feature`

If a feature is completed, it is merged into `develop` after another developer
has reviewed the code and approved the
changes.

If `develop` contains all required features for a release, a `release` branch is
forked from `develop`. The `release`
branch name is constructed according to the following scheme:

`release/[short-description]`

On `release` only bugfixes and related changes are made, which are continuously
merged back to `develop`. Likewise to
the `feature` branches, bugfix branches should follow the same naming
convention:

`bugfix/[Bug-Ticket number from Jira]-short-description-of-bug`

If the `release` branch is ready for deployment, is merged into `main` and
tagged with a version number. After
merging into `main`, the `release` branch is no longer of use and can be
deleted.

`hotfix` branches are required when a deployed version has a critical bug, that
needs to be fixed as soon as possible.
A `hotfix` branch is branched off from `main` and merged back into `main` as
well as into `develop`. When merging
a `hotfix` branch into `main` the version number should be incremented. The
naming convention for a hotfix branch is:

`hotfix/[short-description]`

### Merging policies

All features and bugfixes can only be merged into `develop` and `main` when
another developer has reviewed and approved
the code changes. For this purpose the developer has to create a Pull Request (
PR). Additionally, to the code review,
the reviewer checks if new code segments are tested properly by unit, widget and
integration tests, if needed. The
pipeline and thus the tests must also have been successfully ran for the PR to
be merged. The PR itself is then merged
by the creator and the corresponding branch deleted to keep the repository in a
clean state.

We are using Git Rebase to integrate changes from one branch into another. With
Rebase commits are written from a source
branch onto the top of a target branch. When rebasing a feature branch onto
develop, the git history is kept clean and
linear.

