import Tabs from '@theme/Tabs';
import TabItem from '@theme/TabItem';
import CodeBlock from '@theme/CodeBlock';
import Predicate from '!!raw-loader!@site/docs/generated/js/realm-query-language.codeblock.predicate.js';
import Subquery from '!!raw-loader!@site/docs/generated/js/realm-query-language.codeblock.subquery.js';
import KotlinRQLExample from '!!raw-loader!@site/docs/generated/kotlin/RQLTest.codeblock.rql-examples.kt';
import SwiftRQLExample from '!!raw-loader!@site/docs/generated/swift/SwiftRQLExample.swift';
import DotnetRQLExample from '!!raw-loader!@site/docs/generated/dotnet/RqlSchemaExamples.codeblock.rql-schema-examples.cs';
import NodeRQLExample from '!!raw-loader!@site/docs/generated/node/rql-data-models.codeblock.rql-data-models.js';
import FlutterRQLExample from '!!raw-loader!@site/generated/flutter/task_project_models_test.codeblock.task-project-models.dart';
import Comparison from '!!raw-loader!@site/docs/generated/js/realm-query-language.codeblock.comparison-operators.js';
import Logical from '!!raw-loader!@site/docs/generated/js/realm-query-language.codeblock.logical-operators.js';
import SDL from '!!raw-loader!@site/docs/generated/js/realm-query-language.codeblock.sort-distinct-limit.js';
import Set from '!!raw-loader!@site/docs/generated/js/realm-query-language.codeblock.set-operators.js';
import Aggregate from '!!raw-loader!@site/docs/generated/js/realm-query-language.codeblock.aggregate-operators.js';
import String from '!!raw-loader!@site/docs/generated/js/realm-query-language.codeblock.string-operators.js';

## Overview

Realm Database supports a string-based query language to constrain
searches when retrieving objects. Queries evaluate a predicate for every
object in the collection being queried. If the predicate resolves
to `true`, Realm Database includes the object in the results
collection.

You can use Realm Query Language in most Realm SDKs with your SDK's filter
or query methods. The Swift SDK is the exception, as it uses the NSPredicate
query API.

You can also use Realm Query Language to browse for data in
[Realm Studio](https://www.mongodb.com/docs/realm/studio/#std-label-realm-studio).

## Expressions

Filters consist of **expressions** in a predicate. An expression consists of
one of the following:

- The name of a property of the object currently being evaluated.
- An operator and up to two argument expression(s). For example, in the
  expression `A + B`, the entirety of `A + B` is an expression, but `A`
  and `B` are also argument expressions to the operator `+`.
- A value, such as a string (`'hello'`) or a number (`5`).

<CodeBlock language="javascript">{Predicate}</CodeBlock>

## Dot Notation

When referring to an object property, you can use **dot notation** to refer
to child properties of that object. You can even refer to the properties of
embedded objects and relationships with dot notation.

For example, consider a query on an object with a `workplace` property that
refers to a Workplace object. The Workplace object has an embedded object
property, `address`. You can chain dot notations to refer to the zipcode
property of that address:

```javascript
workplace.address.zipcode == 10012;
```

## Subqueries

You can iterate through a collection property with another query using the
`SUBQUERY()` predicate function. `SUBQUERY()` has the following signature:

```javascript
SUBQUERY(<collection>, <variableName>, <predicate>)
```

- `collection`: the name of the property to iterate through
- `variableName`: a variable name of the current element to use in the subquery
- `predicate`: a string that contains the subquery predicate. You can use the
  variable name specified by `variableName` to refer to the currently-iterated
  element.

A subquery iterates through the given collection and checks the given
predicate against each object in the collection. The predicate may refer
to the current iterated object with the variable name passed to
`SUBQUERY()`.

A subquery expression resolves to an array. Realm Database only
supports the `@count` [aggregate operator](#aggregate-operators)
on this result. This allows you to count how
many objects in the subquery input collection matched the predicate.

You can use the count of the subquery result as you would any other
number in a valid expression. In particular, you can compare the count
with a number literal (such as `0`) or another property (such as
`quota`).

### Example

The following example shows two filters on a `projects` collection.

- The first returns projects with tasks that have not been completed by a user named Alex.
- The second returns the projects where the number of completed tasks is greater than or equal to the project's quota value.

<CodeBlock language="javascript">{Subquery}</CodeBlock>

### About the Examples On This Page

The examples in this page use a simple data set for a task list app.
The two Realm object types are `Project` and `Task`. A `Task`
has a name, assignee's name, and completed flag. There is also an
arbitrary number for priority -- higher is more important -- and a
count of minutes spent working on it. A `Project` has zero or more
`Tasks` and an optional quota for minimum number of tasks expected
to be completed.

See the schema for these two classes, `Project` and `Task`, below:

<Tabs groupId="sdks">
<TabItem value="java" label="Java SDK">
<Tabs groupId="java-sdk-languages">
<TabItem value="java" label="Java">

```java
public class Task extends RealmObject {
  ObjectId id  = new ObjectId();
  String name;
  Boolean isComplete = false;
  String assignee;
  Integer priority = 0;
  Integer progressMinutes = 0;
}

public class Project extends RealmObject {
  ObjectId id = new ObjectId();
  String name;
  RealmList<Task> tasks;
  Integer quota = null;
}
```

</TabItem>

<TabItem value="kotlin" label="Kotlin">

```kotlin
open class Task(): RealmObject() {
  var id: ObjectId = new ObjectId()
  lateinit var name: String
  var isComplete: Boolean = false
  var assignee: String? = null
  var priority: Int = 0
  var progressMinutes: Int = 0
}
open class Project(): RealmObject() {
  var id: ObjectId = new ObjectId()
  lateinit var name: String
  lateinit var tasks: RealmList<Task>
  var quota: Int? = null
}
```

</TabItem>

</Tabs>
</TabItem>

<TabItem value="swift" label="Swift SDK">
  <CodeBlock language="swift">{SwiftRQLExample}</CodeBlock>
</TabItem>
<TabItem value="dotnet" label=".NET SDK">
  <CodeBlock language="dotnet">{DotnetRQLExample}</CodeBlock>
</TabItem>
<TabItem value="node" label="Node.js SDK">
  <CodeBlock language="javascript">{NodeRQLExample}</CodeBlock>
</TabItem>
<TabItem value="react-native" label="React Native SDK">
  <CodeBlock language="javascript">{NodeRQLExample}</CodeBlock>
</TabItem>
<TabItem value="kotlin" label="Kotlin SDK">
  <CodeBlock language="kotlin">{KotlinRQLExample}</CodeBlock>
</TabItem>
<TabItem value="flutter" label="Flutter SDK">
  <CodeBlock language="dart">{FlutterRQLExample}</CodeBlock>
</TabItem>
</Tabs>

## Operators

There are several types of operators available to query a
Realm collection. With these operators, you can:

- Compare values
- Perform logical evaluations
- Compare string values
- Aggregate collection properties
- Evaluate sets
- Sort and limit results

### Comparison Operators

The most straightforward operation in a search is to compare
values.

:::important Types Must Match

The type on both sides of the operator must be equivalent. For
example, comparing an ObjectId with string will result in a precondition
failure with a message like:

```
"Expected object of type object id for property 'id' on object of type
'User', but received: 11223344556677889900aabb (Invalid value)"
```

You can compare any numeric type with any other numeric type,
including decimal, float, and Decimal128.
:::

<table>
<tr>
<th>

Operator

</th>
<th>

Description

</th>
</tr>
<tr>
<td>

`between`

</td>
<td>

Evaluates to `true` if the left-hand numerical or date expression is between or equal to the right-hand range. For dates, this evaluates to `true` if the left-hand date is within the right-hand date range.

</td>
</tr>
<tr>
<td>

`==`, `=`

</td>
<td>

Evaluates to `true` if the left-hand expression is equal to the right-hand expression.

</td>
</tr>
<tr>
<td>

`>`

</td>
<td>

Evaluates to `true` if the left-hand numerical or date expression is greater than the right-hand numerical or date expression. For dates, this evaluates to `true` if the left-hand date is later than the right-hand date.

</td>
</tr>
<tr>
<td>

`>=`

</td>
<td>

Evaluates to `true` if the left-hand numerical or date expression is greater than or equal to the right-hand numerical or date expression. For dates, this evaluates to `true` if the left-hand date is later than or the same as the right-hand date.

</td>
</tr>
<tr>
<td>

`in`

</td>
<td>

Evaluates to `true` if the left-hand expression is in the right-hand list or string.

</td>
</tr>
<tr>
<td>

`<`

</td>
<td>

Evaluates to `true` if the left-hand numerical or date expression is less than the right-hand numerical or date expression. For dates, this evaluates to `true` if the left-hand date is earlier than the right-hand date.

</td>
</tr>
<tr>
<td>

`<=`

</td>
<td>

Evaluates to `true` if the left-hand numeric expression is less than or equal to the right-hand numeric expression. For dates, this evaluates to `true` if the left-hand date is earlier than or the same as the right-hand date.

</td>
</tr>
<tr>
<td>

`!=`, `<>=`

</td>
<td>

Evaluates to `true` if the left-hand expression is not equal to the right-hand expression.

</td>
</tr>
</table>

#### Example

The following example uses the query engine's
comparison operators to:

- Find high priority tasks by comparing the value of the `priority` property value with a threshold number, above which priority can be considered high.
- Find long-running tasks by seeing if the `progressMinutes` property is at or above a certain value.
- Find unassigned tasks by finding tasks where the `assignee` property is equal to `null`.
- Find tasks within a certain time range by finding tasks where the `progressMinutes` property is between two numbers.

<CodeBlock language="javascript">{Comparison}</CodeBlock>

### Logical Operators

You can make compound predicates using logical operators.

<table>
<tr>
<th>

Operator

</th>
<th>

Description

</th>
</tr>
<tr>
<td>

`AND`, `&&`

</td>
<td>

Evaluates to `true` if both left-hand and right-hand expressions are `true`.

</td>
</tr>
<tr>
<td>

`NOT`, `!`

</td>
<td>

Negates the result of the given expression.

</td>
</tr>
<tr>
<td>

`OR`, `||`

</td>
<td>

Evaluates to `true` if either expression returns `true`.

</td>
</tr>
</table>

#### Example

We can use the query language's logical operators to find
all of Ali's completed tasks. That is, we find all tasks
where the `assignee` property value is equal to 'Ali' AND
the `isComplete` property value is `true`:

<CodeBlock language="javascript">{Logical}</CodeBlock>

### String Operators

You can compare string values using these string operators.
Regex-like wildcards allow more flexibility in search.

#### Case Insensitivity

You can use the following modifiers with the string operators:

- `[c]` for case insensitivity.

```javascript
"name CONTAINS[c] 'a'";
```

<table>
<tr>
<th>

Operator

</th>
<th>

Description

</th>
</tr>
<tr>
<td>

`beginsWith`

</td>
<td>

Evaluates to `true` if the left-hand string expression begins with the right-hand string expression. This is similar to `contains`, but only matches if the right-hand string expression is found at the beginning of the left-hand string expression.

</td>
</tr>
<tr>
<td>

`in`

</td>
<td>

Evaluates to `true` if the left-hand string expression is found anywhere in the right-hand string expression.

</td>
</tr>
<tr>
<td>

`contains`

</td>
<td>

Evaluates to `true` if the right-hand string expression is found anywhere in the left-hand string expression.

</td>
</tr>
<tr>
<td>

`endsWith`

</td>
<td>

Evaluates to `true` if the left-hand string expression ends with the right-hand string expression. This is similar to `contains`, but only matches if the left-hand string expression is found at the very end of the right-hand string expression.

</td>
</tr>
<tr>
<td>

`like`

</td>
<td>

Evaluates to `true` if the left-hand string expression matches the right-hand string wildcard string expression. A wildcard string expression is a string that uses normal characters with two special wildcard characters, including `*` (matches zero or more of any character) and `?` (matches any character). For example, the wildcard string "d?g" matches "dog", "dig", and "dug", but not "ding", "dg", or "a dog".

</td>
</tr>
<tr>
<td>

`==`, `=`

</td>
<td>

Evaluates to `true` if the left-hand string is lexicographically equal to the right-hand string.

</td>
</tr>
<tr>
<td>

`!=`, `<>`

</td>
<td>

Evaluates to `true` if the left-hand string is not lexicographically equal to the right-hand string.

</td>
</tr>
</table>

#### Example

We use the query engine's string operators to find:

- Projects with a name starting with the letter 'e'
- Projects with names that contain 'ie'

<CodeBlock language="javascript">{String}</CodeBlock>

### Aggregate Operators

You can apply an aggregate operator to a collection property of a Realm
object. Aggregate operators traverse a collection and reduce it to a
single value.

<table>
<tr>
<th>

Operator

</th>
<th>

Description

</th>
</tr>
<tr>
<td>

`@avg`

</td>
<td>

Evaluates to the average value of a given numerical property across a collection.
If any values are `null`, they are not counted in the result.

</td>
</tr>
<tr>
<td>

`@count`

</td>
<td>

Evaluates to the number of objects in the given collection.

</td>
</tr>
<tr>
<td>

`@max`

</td>
<td>

Evaluates to the highest value of a given numerical property across a collection.
`null` values are ignored.

</td>
</tr>
<tr>
<td>

`@min`

</td>
<td>

Evaluates to the lowest value of a given numerical property across a collection.
`null` values are ignored.

</td>
</tr>
<tr>
<td>

`@sum`

</td>
<td>

Evaluates to the sum of a given numerical property across a collection,
excluding `null` values.

</td>
</tr>
</table>

#### Example

These examples all query for projects containing tasks that meet
this criteria:

- Projects with average task priority above 5.
- Projects with a task whose priority is less than 5.
- Projects with a task whose priority is greater than 5.
- Projects with more than 5 tasks.
- Projects with long-running tasks.

<CodeBlock language="javascript">{Aggregate}</CodeBlock>

### Collection Operators

A **collection operator** uses specific rules to determine whether
to pass each input collection object to the output
collection by applying a given predicate to every element of
a given list property of
the object.

<table>
<tr>
<th>

Operator

</th>
<th>

Description

</th>
</tr>
<tr>
<td>

`ALL`

</td>
<td>

Returns objects where the predicate evaluates to `true` for all objects in the collection.

</td>
</tr>
<tr>
<td>

`ANY`, `SOME`

</td>
<td>

Returns objects where the predicate evaluates to `true` for any objects in the collection.

</td>
</tr>
<tr>
<td>

`NONE`

</td>
<td>

Returns objects where the predicate evaluates to false for all objects in the collection.

</td>
</tr>
</table>

#### Example

We use the query engine's collection operators to find:

- Projects with no complete tasks.
- Projects with any top priority tasks.

<CodeBlock language="javascript">{Set}</CodeBlock>

### Sort, Distinct, Limit

You can use additional operators in your queries to sort and limit the
results collection.

<table>
<tr>
<th>

Operator

</th>
<th>

Description

</th>
</tr>
<tr>
<td>

`SORT`

</td>
<td>

Specify the name of the property to compare. You can optionally
specify ascending (`ASC`) or descending (`DESC`) order.
If you specify multiple SORT fields, the query sorts by the first
field, and then the second. For example, if you `SORT (priority, name)`,
the query returns sorted by priority, and then by name when priority
value is the same.

</td>
</tr>
<tr>
<td>

`DISTINCT`

</td>
<td>

Specify a name of the property to compare. Remove duplicates
for that property in the results collection. If you specify multiple
DISTINCT fields, the query removes duplicates by the first field, and
then the second. For example, if you `DISTINCT (name, assignee)`,
the query only removes duplicates where the values of both properties
are the same.

</td>
</tr>
<tr>
<td>

`LIMIT`

</td>
<td>

Limit the results collection to the specified number.

</td>
</tr>
</table>

#### Example

We use the query engine's sort, distinct, and limit operators to find:

- Tasks where the assignee is Ali

  - Sorted by priority in descending order
  - Enforcing uniqueness by name
  - Limiting the results to 5 tasks

<CodeBlock language="javascript">{SDL}</CodeBlock>
