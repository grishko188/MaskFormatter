# MaskFormatter
Formatter library that provides the ability to format the text for any arbitrarily given mask,
and to clean the filter.
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
<li>Do not use the mask type ####z ( any letter or number after replacement sequence without space after last replacement char)</li>
<li>Do not use the mask type #### #### #### z ( any characters after replacement sequence in the end of mask) with EditText formatting(you will have troubles with removing), but works fine for static formatting</li>
</ul>
