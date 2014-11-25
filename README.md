# FailFast (Experimental)
[![Gitter](https://badges.gitter.im/Join Chat.svg)](https://gitter.im/nhaarman/FailFast?utm_source=badge&utm_medium=badge&utm_campaign=pr-badge&utm_content=badge)
*FailFast* provides implicit contract checks to your methods.

IntelliJ and Android Studio provide static inspections for methods and parameters annotated with `@NotNull`. When a method is called with a `null` value to a parameter marked as `NotNull`, a warning is presented. 

This is not enforced however, and `null` can be passed, possibly leading to errors which can be hard to debug. A solution is to check each and every parameter which is annotated as `NotNull`:

```java
public void myMethod(@NotNull final String message){
	if(message == null) {
		throw new NullPointerException("message == null");
	}

	/* Rest of method */
}
```

This fast fail method is an excellent way to prevent those hard to debug errors. Unfortunately, it requires you writing a lot of code, and it doesn't get your code any cleaner.

*FailFast* injects these snippets for you compile time, so you don't have to, and your source code stays clean.

## Usage
Just annotate your methods and parameters with the `@NotNull` annotation, and *FailFast* will do the rest:

```java
@NotNull
public String concatenate(@NotNull final String first, @NotNull final String second) {
	return first.concat(second);
}
```

Whenever the `concatenate` method is called with a `null` value, `FailFast` will throw a `NullPointerException`:

```java
concatenate("Hello", null);

java.lang.NullPointerException: Parameter "second" of method "concatenate" was specified as @NotNull, but null was given.
	at com.myApp.MyActivity.onCreate(MyActivity.java:19)
```

## Setup
```groovy
buildscript {
  repositories {
    mavenCentral()
  }

  dependencies {
    classpath 'com.nhaarman.failfast:plugin:0.0.1'
  }
}

apply plugin: 'com.android.application'
apply plugin: 'com.nhaarman.failfast'
```

## Supported annotations:
 - `org.jetbrains.annotations.NotNull`
 - `android.support.annotation.NonNull`

## Contribute
Please do! I'm happy to review and accept pull requests.  
Please read [Contributing](https://github.com/nhaarman/FailFast/blob/master/CONTRIBUTING.md) before you do.

## Developed By
* Niek Haarman

## Special Thanks
* Jake Warthon - Project setup is based on [Hugo](https://github.com/JakeWharton/hugo).
* [Contributors](https://github.com/nhaarman/FailFast/graphs/contributors)

## License

	Copyright 2014 Niek Haarman

	Licensed under the Apache License, Version 2.0 (the "License");
	you may not use this file except in compliance with the License.
	You may obtain a copy of the License at

	http://www.apache.org/licenses/LICENSE-2.0

	Unless required by applicable law or agreed to in writing, software
	distributed under the License is distributed on an "AS IS" BASIS,
	WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
	See the License for the specific language governing permissions and
	limitations under the License.
