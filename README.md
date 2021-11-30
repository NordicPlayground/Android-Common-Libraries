# Android-Common-Libraries
A libraries with Nordic's common code for Android apps.

## Theme
The library may be found on Maven Central repository. Add it to your project by adding the following dependency:
```Groovy
implementation 'no.nordicsemi.android.common:theme:1.0.0'
```

The library contains a few features which are necessary for theming Nordic Semiconductor apps:
* Color palette adopted to the new Material You
* Typography
* Theme for dark and light mode
* Abstract NordicActivity class which contain implementations for Nordic's splash screen animation.
* Custom implementation of missing components from Material You compose library. The intention is to replace them in the future with the original implementation. Below components contains custom implementation:
   - Card
   - Checkbox
   - CircularProgressIndicator - currently implemented as a static icon
   - RadioButtonGroup & RadioButton
