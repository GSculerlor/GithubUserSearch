# Github User Search App

## Goal

An Android application which can searching for Github user.

<img src="assets\Screenshot_1598961100.png" alt="Screenshot_1598961100" style="zoom: 25%;" /><img src="assets\Screenshot_1598961084.png" alt="Screenshot_1598961084" style="zoom:25%;" /> 

## Constraint

- App must run on API 19+ (Kitkat and above)
- App must have an infinite scrolling feature
- App should be able to search for any query

## Solution

### Architecture

This app applying Android best practice architecture, model-view-view model (MVVM). This app separated into layers, in this case I use `PresentationDomainDataLayering` layering, each package indicates/represents a different concern/topic: UI logic (presentation), persistence logic, and business (and application) logic. Here, I also use Repository pattern, which mean there is only a single source of truth that managed on repository, and I utilize a use-case as a mediator between view model and repository.

![image-20200901185952827](assets\image-20200901185952827.png)

### Library and tech stacks

As stated on architecture, this app follow Android best practice, including the library that used. Here, I tried to make this app as a "single activity application" using Navigation component from androidx. Since we're dealing with lifecycle aware component, I also included lifecycle component from androidx to handle it (including handling livedata observation). For the data itself, I use benefits of Kotlin coroutines's flow that works well with new paging component from androidx. This app also implement dependency injection pattern to avoid leaks at any cost. The dependency injection library used here is Koin. For network stack, since we're dealing with Github API, I use Apollo GraphQL to handle GraphQL query to Github endpoints and also okhttp for network request. For the UI and data showing library, I simply use data binding to reduce findviewbyid boilerplate and make our code cleaner and easier to check.

### Edge Cases

#### Network related edge cases

New paging library from androidx have a layered source via `RemoteMediator`, object that handles paging from a layered data source, such as a network data source with a local database cache. By observing `LoadState` from paging, we can determine if our request is hitting a success or not. If there's an error (http call problem, no internet, etc), UI will be showing a toast with the error message. Another edge case that can be happened is no connection or error when we load next page of the list. This case handled by showing a retry button when app can't load next batch of users. 

<img src="assets\Screenshot_1598963158.png" alt="Screenshot_1598963158" style="zoom: 25%;" /><img src="assets\Screenshot_1598963154.png" alt="Screenshot_1598963154" style="zoom:25%;" />

#### API response edge cases

There are two edge cases that need to handled. First, there are some possibility that can be happened. First is when your query have no result. For this case, the app will be showing a `TextView` simply to indicate that there's no result for the given query. The other case is Github response for searching user actually not only returning user, but also organization. This case fixed by add organization fragment to our search query. By this, we can simply map user and organization as a result for the query.

<img src="assets\Screenshot_1598963395.png" alt="Screenshot_1598963395" style="zoom:25%;" /><img src="assets\Screenshot_1598963383.png" alt="Screenshot_1598963383" style="zoom:25%;" />

### Drawback and Limitation

When working for this test, I use paging version `3.0.0-alpha05`, when this version give a lot of improvement (for example it make it easier to handle network response rather than use common component like `NetworkBoundResource` and `Resource<T>` from Android example repository) it give a lot of issue when dealing with the unit test since there's very little documentation about how to unit test using this version of pagination. Instead, in this app I simply add an instrumentation test as a simple sanity check if the result is displayed or not.

Another drawback is since our constraint is this app should run on API 19 devices, I need to handle the Okhttp dependency to support this API version since the latest one is already not supporting API 19. Okhttp version strictly set to `3.12.12` to support API 19. And as an additional step to tackle it, I add support for `TLSv1.1` and `TLSv1.2`

## Building this app

You can simply clone it and load it on Android Studio. Make sure you add your Github personal token at `NetworkModule.kt`