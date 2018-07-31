# MaskFormatter
Formatter library that provides the ability to format the text for any arbitrarily given mask,
and to clean the filter.

Getting Started
==============

To add MaskFormatter library to your project, add the following to your app module's ```build.gradlew```
```
allprojects {
	repositories {
		...
	    maven { url 'https://www.jitpack.io' }
	}
}
	
dependencies {
    	implementation 'com.github.grishko188:MaskFormatter:1.1.1'
}
```

<br/>
<br/>
<b>Version 1.1.2 (What's new)</b>
<li>Add method 

```useMaskPrefixNecessarily(boolean value)```. 

Switch if mask prefix should be always in input.
This parameter makes the mask prefix not removed from the input field.
Generally designed to use with {@link MaskTextWatcher}
By default this parameter is false.
<br/>
<br/>
<b>Version 1.1.0 (What's new)</b>
<ul>
<li>Add method 

```ignoreInputPrefixes(String... prefixes)```. 

Created to avoid duplicate characters if yout input starts with the same prefix just like the mask</li>
<li>Add method 

```maskPrefix(String prefix)```. 

Created to build complicated mask like PHONE_COUNTRY_CODE + PHONE_FORMAT. For example maskPrefix +3 80 , and mask format (###) ###-##-##. It gives you more flexibility for building complicated formats.  </li>
<li>Add method 

```strictMask(boolean isStict)```. 

Created to make mask non-strict. For example your prefer some mask like ## ## ####. But if yout input is longer then mask - formatting will be cleared.</li>
<li>Add new constructor 

``` new MaskTextWatcher(MaskFormatter configuredFormatter)```

, to create text watcher with the specific MaskFormatter
</ul>

<br/>Contains to classes <b>MaskFormatter</b> and <b>MaskTextWatcher</b>
<br/>
<br/>
Use: 
<ul>
<li><b>MaskFormatter</b> for simple string formatting with the given mask </li>
<li><b>MaskTextWatcher</b> for formatting user input with the given mask</li>
</ul>
<br/>
<br/>
Usage for <b>MaskFormatter</b>

```
   MaskFormatter.get()
             .symbol(symbol)
             .mask(mask)
             .ignoreInputPefixes("input_prefix")
             .maskPrefix("prefix")
             .strictMask(false)
             .format(source);
```
<br/>
Usage for <b>MaskTextWatcher</b>

```
mTextInput.addTextChangedListener(new MaskTextWatcher("#### #### #### ####"));
```
<br/><br/>
Known issues:
<ul>
<li>Do not use the mask type ####z ( any letter or number after replacement sequence without space after last replacement char)
 If you need to format and clear such mask, use <b>MaskFormatter#clearStatic(String)</b> instead, but only for formatting static text(not allowed for user input formatting).</li>
<li>Do not use the mask type #### #### #### z ( any characters after replacement sequence in the end of mask) with EditText formatting(you will have troubles with removing), but works fine for static formatting</li>
</ul>
