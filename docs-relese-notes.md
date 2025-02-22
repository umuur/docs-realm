# April 29, 2022

## SDK Docs

### .Net SDK
Usage Examples
- Open a Realm: Make `FlexibleSyncConfiguration` a link to API docs
- Authenticate Users: Add section about "Offline Login"

### Node.js SDK
- Usage Examples -> Open and Close a Realm: Add documentation for opening local realm as synced and synced realm as local
- Fix broken JS/TS tests

### React Native SDK
- Usage Examples -> Open and Close a Realm: Add documentation for opening local realm as synced and synced realm as local

### Flutter SDK
- Move Flutter SDK pages about Realm Database to a new Realm Database section

### Web SDK
- MongoDB Realm -> Manage Email/Password Users: Correct a link to retry a user confirmation function

## MongoDB Cloud Docs

### Schemas
- Enforce a Schema: add a section about validating null types
- Relationships: add a note about the fact that relationships cannot span partitions

### Functions
- JavaScript Support: New page that combines "Built-In Module Support" & "JavaScript Feature Compatibility"
- External Dependencies: New page that combines "Add External Dependencies" and "Import External Dependencies"

Query MongoDB Atlas
- Move the Query MongoDB Atlas section to Functions in the navigation
- Rename "Find Documents" to "Read Data from MongoDB Atlas - Functions" & expand an example
- Aggregate: Rename "Run Aggregation Pipelines" to "Aggregate Data in MongoDB Atlas - Functions", expand code example

### Logs
- View Logs in the Realm UI: New page

### Reference
- Realm Admin API: Add `unordered` field to DB triggers
- Realm Query Language: Remove `@avg` from Flex Sync supported operation on array fields
- Service Limitations: Update Request Traffic limits info

# April 22, 2022

## SDK Docs

### Java SDK
Usage Examples
- Display Collections: Refactor to improve readability
- Reset a Client Realm: Refactor to improve readability

### Node.js SDK
- Fundamentals -> Write Transactions: Refactor to improve readability

### React Native SDK
- Fundamentals -> Write Transactions: Refactor to improve readability
- Advanced Guides -> Bundle a Realm: Update the page for details specific to RN vs. Node.js process

## MongoDB Cloud Docs

### Realm Sync
Define Data Access Patterns
- Partition-Based Sync: Refactor to improve readability
- Flexible Sync: Update to add arrays of primitives as valid queryable fields

### MongoDB Data Access
Atlas Clusters & Data Lakes -> Document Preimages: Refactor to improve readability

### HTTPS Endpoints
- Create an HTTPS Endpoint: Clarify Webhook vs. HTTPS endpoint body behavior

### Functions
- Context: New page that combines "Access Function Context" and "Context Modules" pages
- Global Modules: New page that combines "Utility Packages" and "JSON & BSON" pages

### Triggers
- About Triggers: Refactor to improve readability

### Reference
- Realm Query Language: Update Flex Sync Unsupported Query Operators (Remove `BETWEEN`, `BEGINSWITH`, `ENDSWITH`, `CONTAINS` as these are now supported)

### Third-Party Services [Deprecated]
- Convert Webhooks to HTTPS Endpoints: Clarify Webhook vs. HTTPS endpoint body behavior

# April 15, 2022

## SDK Docs

### Swift SDK
- Fundamentals -> Realms: Typo fix
- Advanced Guides -> Client Reset: Incorporate tech review feedback
- Usage Examples -> Define a Realm Object Schema: add a note about to-one relationships being optional

### Node.js SDK
- Usage Examples
  - Sync Changes Between Devices: clarify session multiplexing, clarify description of params for syncSession.addProgressNotification()
  - Open & Close a Realm: Typo fix
- Fundamentals -> Realms: Typo fix

### React Native SDK
- Usage Examples
  - Sync Changes Between Devices: clarify session multiplexing, clarify description of params for syncSession.addProgressNotification()

### Web SDK
- Add API Reference docs to Web SDK sidebar
- Remove "Reference" from sidebar and move its two pages up to the main Web SDK sidebar menu
- Fix broken link on main page

### Flutter SDK
- Define Realm Object Schema: fix broken link

## MongoDB Cloud Docs

### MongoDB Data Access
- Atlas Clusters & Data Lakes
  - Rename "Specify Read Preference for a MongoDB Cluster" to "Read Preference"
  - Combine "Enable Wire Protocol Connections" and "Connect Over the Wire Protocol" into a new page called "Wire Protocol"
  - Rename "View & Disable Collection-level Preimages" to "Document Preimages"
  - New Internal Database Usage page with details about system-generated cluster users, transactional locks, and unsynced documents

### MongoDB Realm
- Users & Authentication
  - Authentication Providers: Refactor to improve readability
  - Custom JWT Authentication: Add a Firebase JWT Authentication guide
- Realm Sync: Add client reset warnings to pages that mention terminating sync
  - Define Data Access Patterns -> Partition-based Sync
  - Configure and Update Your Data Model -> Make Breaking Schema Changes
  - Handle Errors -> Client Resets: Add a note about upgrading a cluster causing a client reset
  - Reference -> Upgrade a Shared Tier Cluster

### Admin API
- Create Functions endpoint: update response body
- Create an Endpoint and Modify an Endpoint: fix broken links

Many Pages Across Docs
- Remove alt apostrophe character ’ and replace with '

# April 8, 2022

## SDK Docs

### Java SDK
- Removed no-longer-needed certificate warning for Android 6.0 and below
- Streamlined install page, added guidance on installation for projects that use Gradle's plugin syntax

### .Net SDK
- Fixed list of primary key/indexable types

### Node.JS SDK
- Fixed list of primary key/indexable types

### React Native SDK
- Fixed list of primary key/indexable types

## MongoDB Cloud Docs

### Reference -> Realm Query Language 
- Fixed @count limitations

### Manage & Deploy Realm Apps -> Create -> Template Apps
- Cleaned up wording, typos, linkage in the Template Apps page

### Functions
- Updated wording of timeout description to make timeout period more discoverable

# April 1, 2022

## SDK Docs

### Java SDK
- Encrypt a Realm: Add a section about not accessing an encrypted realm from multiple processes
- Sync a Realm in the Background: New page w/tested code examples for syncing Realm in the background
- Link User Identities: Update info and code examples to provide additional guidance

### Swift SDK
- Encrypt a Realm: Add a note to the section about not accessing an encrypted realm which describes the error
- Update several pages with a version requirement table to use Swift async/await syntax/APIs
- Client Reset: Add docs for new client reset mode w/the ability to discard local changes for a seamless client reset
- SwiftUI Guide: Additional information around migration when using SwiftUI
- Threading 
  - Add section about realm supporting only serial queues for background threads, not concurrent queues
  - Add note about ThreadSafeReference and @ThreadSafe wrapper conforming to `Sendable`

### .Net SDK
- Encrypt a Realm: Add a section about not accessing an encrypted realm from multiple processes
- Unity: Update known issues:
  - Remove "only supports Intel64 Macs"
  - Clarify issues with multiple processes; specify crashes are related to synced realms only, not local realms

### Node.js SDK
- Encrypt a Realm: Add a section about not accessing an encrypted realm from multiple processes

### React Native SDK
- Test & Debug: New page with recommendations and some basic guidance about testing & debugging
- Encrypt a Realm: Add a section about not accessing an encrypted realm from multiple processes
- Quick Start with Expo: New page w/code examples for using the Realm Expo template to initialize, build and run, and deploy a project
- Use Realm React: New page w/code examples about using the Realm React npm package to use realm through React hooks

### Flutter SDK
- React to Changes: New page about using change listeners w/tested, Bluehawked code examples
- Quick Start: Update Quick Start with tested, Bluehawked code examples for using change listeners

## MongoDB Cloud Docs
- Realm Sync/Define Data Access Patterns/Flexible Sync: Add section for reserved field names
- Users & Authentication
  - Authentication Providers/Anonymous Authentication: Remove refresh token expiration references, emphasize inactive user deletion & explicit logout
  - Enable User Metadata: Refactor to improve readability
- Rules/Expressions
  - Show $Operator over %operator when possible (on this page and in several others)
  - Provide additional information and examples about expansion variables
  - Restructure to remove tables
- Functions/Add External Dependencies: Update `node_module` size cap from 10MB to 15MB
- Values & Secrets: add a link to the info about accessing environment values in a function context
  - Access a Value: add a link to the info about accessing environment values in a function context

# March 25, 2022

## SDK Docs

### Java SDK
- Add note about ISRG Root X1 certificates for Android 6 and lower devices to Install and Troubleshooting pages
- Updates for the generated Java API reference (fix links, add titles to index pages for breadcrumb reasons)
- Updates based on feedback from engineering

### Swift SDK
- Quick Start - Realm in Xcode Playgrounds: New page describing how to use Realm in Xcode Playgrounds, organize Quick Starts under a ToC item
- Fundamentals -> Relationships: Refactor to improve readability
- Authenticate: Update Facebook Auth code example to use current version of FacebookLogin
- iOS Test Suite: Switch dependency manager from CocoaPods to Swift Package Manager
- Collections: Update AnyRealmCollection code example which broke in a recent release
- Update code example for encrypted realms which broke in a recent release
- Authenticate Users: Add code example for Google User showing ID Token authentication

### .Net SDK
- Sync Changes Between Devices: Add note about HelpLink and clarify wording of a few other notes

### Kotlin SDK
- Updates based on feedback from engineering

### Flutter SDK
- Open and Close a Realm: Add sections for read-only realm, in-memory realm, set custom FIFO special file path
- Read and Write Data: Add docs for querying RealmList of RealmObjects, remove note about fllutter for Linux desktop not being supported
- Data Types: New page listing supported data types
- Define a Realm Object Schema: New page describing how to define a Realm Object Schema

## MongoDB Cloud Docs

### Manage & Deploy Realm Apps
- Create -> Template Apps: Add SwiftUI template app to the list of available template apps
- Application Security
  - Add note clarifying the Firewall Configuration IP list only applies to outgoing requests from functions/triggers/HTTPS endpoints, not requests that originate from the Sync server
  - Add note about communication between Realm and Atlas being encrypted with x509 certificates

### Users & Authentication
- Authentication Providers -> Custom Function Authentication: Provide clearer guidance on `identities`, general updates & improvements

Many Docs Pages
- As part of the subdomain consolidation project, many folks updated many URLs across Realm docs pages. Nice work, everyone!
- Various dependabot updates

# March 18, 2022

## Get Started
- Getting Help: Expand description of Professional Support & Forums

## SDK Docs

### Java SDK
- Query Engine: Add link to RQL reference, info about when to use Java SDK Fluent Interface vs. RQL

### Swift SDK
- SwiftUI Guide: 
  - Add migration section w/examples
  - Add section about Filtering w/new ObservedResults type-safe query example
- Rename "Reference Manual" in sidebar to "API Reference"

### .Net SDK
- Update Realm .Net version
- Query Engine: Add info for when to use LINQ and when to use RQL
- Flexible Sync: Add link to query engine page, provide info about `Add` method, clarify duplicate subscriptions note
- Rename "Reference Manual" in sidebar to "API Reference"
- Sync Changes Between Devices: Update API reference link and code example for setting log level

### Node.js SDK
- Query Engine: Add link to RQL reference
- Flexible Sync: Add info about supported queries, link to RQL Flex Sync Limitations
- Rename "Reference Manual" in sidebar to "API Reference"
- Update deprecated `serverApiKey` usage to `apiKey` in examples
- Refactor data type unit tests to simplify Bluehawk commands and remove persistent state

### React Native SDK
- Query Engine: Add link to RQL reference
- Flexible Sync: Add info about supported queries, link to RQL Flex Sync Limitations, update Flexible Sync subscription syntax
- Rename "Reference Manual" in sidebar to "API Reference"
- Update deprecated `serverApiKey` usage to `apiKey` in examples
- Refactor data type unit tests to simplify Bluehawk commands and remove persistent state

### Kotlin SDK
- Install process fixes - the definitive version
- Rename "Reference Manual" in sidebar to "API Reference"

### Flutter SDK
- Rename "Reference Manual" in sidebar to "API Reference"

## MongoDB Cloud Docs

### Users & Authentication
- Custom JWT Authentication: add kid header for JWKS

### Sync
- Flexible Sync: 
  - Add link from Eligble Field Types to RQL Reference for Flex Sync query options
  - Add links to Node.js and RN Flexible Sync pages
- Choose Your Sync Mode: Update JS Flex Sync query syntax
- Sync Rules and Permissions: 
  - Update permissions syntax, fix JSON errors in examples
  - Add info about session roles, expansions, client resets, add Flexible Sync Expansion quick-reference table
- Update Your Schema: Add workaround to avoid changing property name breaking change by using mapTo or similar API in the SDKs that offer it
- Make Breaking Schema Changes: Add early return to partner collection funcs to avoid errors
- Client Resets: Add information about Flex Sync session role changes that cause client resets

### Schemas
- Relationships: add embedded object relationship examples

### Reference
- Realm Query Language Reference: Add links to SDK-specific query pages

Many Docs Pages
- As part of the language update changing "additive" and "destructive" schema changes to "non-breaking" and "breaking" - update references on MANY pages across docs


# March 11, 2022

## Tutorial
- Set up the Backend: Add tutorial video as an embedded video
- iOS with Swift: Add tutorial videos as embedded videos to both Part 1 and Part 2
- Write a Serverless GitHub Contribution Tracker: Tutorial updated to use HTTPS endpoints 

## SDK Docs

### Java SDK
- Install: bump required JDK version to 9

### Swift SDK
- Filter Data: Add `IN` type-safe query example per SDK 10.23.0 release

### .Net SDK
- Replace `SyncConfiguration` references with `PartitionSyncConfiguration`
  in copy and code examples

### Kotlin SDK
- Landing Page, Install, Quick Start: small improvements based on SDK team feedback

### Web SDK
- Add GitHub Action to run Web SDK unit tests on relevant PRs

### Flutter SDK
- Read & Write Data: new page with code examples
- Query Language: new page

## MongoDB Cloud Docs

### Sync
- Update a Schema: Major page refactor to more clearly communicate breaking/non-breaking changes to schema and object model
- Make Destructive Schema Changes: naming updated to "Make Breaking Schema Changes" & small tweaks throughout page for naming

### Realm Admin API 
- adminGetLogs API endpoint: Add "type" enum and improve "error_only" information

# March 4, 2022

## SDK Docs

### Java SDK
- Switch Java API Reference to use Yokedox-generated output directly in docs site
- Query MongoDB: new section with use cases for querying MongoDB remotely

### Swift SDK
- Configure & Open a Realm: new sections for converting between local/synced 
  realms/sync configurations
- Bundle a Realm: New procedures for bundling a synced realm
- Create and Delete Users: new page w/code examples for deleting a user
- Work with Users: new section about creating and deleting users
- Query MongoDB: new section with use cases for querying MongoDB remotely

### .Net SDK
- Query MongoDB: new section with use cases for querying MongoDB remotely

### Node.JS SDK
- Sync Changes Between Devices: Additional information about partition value
- Update Jest configuration to make tested code examples work with Realm Sync
- Query MongoDB: new section with use cases for querying MongoDB remotely

### React Native SDK
- Sync Changes Between Devices: Additional information about partition value
- Query MongoDB: new section with use cases for querying MongoDB remotely

### Kotlin SDK (alpha)
- Install: Additional info 
- New section: Realm Database, covering CRUD operations, schemas, open & closing
  a realm, query language, write transactions, and concept information
- New section: Sync, covering concept overview and pening a synced realm
- Migrate from the Java SDK to the Kotlin SDK: New guide w/tested code examples

### Web SDK
- Create Bluehawked & tested code examples for:
  - Work with Multiple Users
  - Create & Manage API Keys
  - Authenticate a User
  - Manage Email/Password Users
  - Link User Identities 

## MongoDB Cloud Docs

### Sync
- Sync Rules and Permissions: Flexible Sync: add links and minor tweaks

### Users
- Create a User: additional information about creating users
- Delete a User: new section about deleting users in the SDK

# Feb 25, 2022

## SDK Docs

### Android SDK
- Rename to Java SDK

### .NET SDK
- For `DateTimeOffset`, note that timezone information is lost

### Node.js SDK
- Fixes for Flexible Sync examples, new Add a Query Flex Sync example
- Add documentation for new .deleteUser() method

### React Native SDK
- Fixes for Flexible Sync examples, new Add a Query Flex Sync example
- Add documentation for new .deleteUser() method

### Kotlin SDK (alpha)
- Add page for Sync, including tested code examples

### Web SDK
- Add documentation for new .deleteUser() method
- Convert Apollo GraphQL code examples to tested, Bluehawked code snippets

## MongoDB Cloud Docs

### Sync
- Flexible Sync Roles renamed Flexible Sync Session Roles, more info to clarify Flexible Sync role & rule behavior
- Flexible Sync Configuration: Remove callout about shared clusters not running MDB 5.0

### Functions
- Define a Function: Clarify that you can define functions inside of nested folders

### Manage Realm Apps
- Export a Realm App with Realm API: Fixes for app export procedure

### Triggers
- Fix a redirect loop

### Realm Admin API
- Custom HTTPS endpoints APIs added
- Typo fixes, rearrange and simplify `tags`, and distinguish `summary` from `description` to improve Redoc implementation


# Feb 18, 2022

## SDK Docs

### Android SDK
- Add Flexible Sync callouts re: required subscription and object links

### Swift SDK
- Publish tutorial videos
- Update the tutorial copyright date
- Add a tip about deleting vs. migrating realm when in development
- Add more guidance around compacting
- Add Flexible Sync callouts re: required subscription and object links
- SwiftUI Quick Start improvements

### .NET SDK
- Add a tip about deleting vs. migrating realm when in development
- Add more guidance around compacting
- Add Flexible Sync callouts re: required subscription and object links

### Node.js SDK
- Add a tip about deleting vs. migrating realm when in development
- Add Flexible Sync callouts re: required subscription and object links
- Refactor Relationships & Embedded Objects page to improve readability

### React Native SDK
- Add a tip about deleting vs. migrating realm when in development
- Add Flexible Sync callouts re: required subscription and object links
- Refactor Relationships & Embedded Objects page to improve readability

### Web SDK
- Update examples to tested examples that use Bluehawk

## MongoDB Cloud Docs

### Logs
- Fix Application Log retention time

### HTTPS Endpoints
- Reorganize and expand the HTTPS Endpoints section
- Add HTTPS Authentication info
- Add migration guide to Convert Webhooks to HTTPS Endpoints
- Update configuration

### Third-Party Services
- Replace Services with npm Modules: Add some guidance + examples for Axios, Twilio, AWS

### Custom User Data
- Add size limit guidance
