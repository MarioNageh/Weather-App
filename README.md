# Android MVVM Architecture: Weather APP
![alt text](https://cdn-images-1.medium.com/max/1600/1*OqeNRtyjgWZzeUifrQT-NA.png)

The main advatage of using MVVM, there is no two way dependency between ViewModel and Model unlike MVP. Here the view can observe the datachanges in the viewmodel as we are using LiveData which is lifecycle aware. The viewmodel to view communication is achieved through observer pattern (basically observing the state changes of the data in the viewmodel).


This repository contains a detailed Weather app that implements MVVM architecture using Retrofit, Gson and Android Architecture Components

<p align="center">
  <img src="https://user-images.githubusercontent.com/36963317/107503583-ce99ec00-6ba2-11eb-82a7-fa28a9b81b21.jpg" width="250">
  <br>
  <img src="https://user-images.githubusercontent.com/36963317/107503576-ccd02880-6ba2-11eb-85d9-ebdf2fc9acf4.jpg" width="250">
 <br>
  <img src="https://user-images.githubusercontent.com/36963317/107503578-ce015580-6ba2-11eb-8484-1f7d85e67580.jpg" width="250">
  <br>
  <img src="https://user-images.githubusercontent.com/36963317/107503579-ce99ec00-6ba2-11eb-88d2-a9076c42c31a.jpg" width="250">
</p>
<br>
<br>

#### The app has following packages:
1. **modules**: It contains all the data accessing and manipulating components.
2. **retrofit**: For establish http for apis.
3. **views**: View classes such Activities,Dialogs.
4. **utils**: Utility classes.
5. **viewModels**:  for preparing and managing the data for an Activity or a Fragment . It also handles the communication of the Activity / Fragment with the rest of the application.

#### Classes have been designed in such a way that it could be inherited and maximize the code reuse.

### Special Constants:
1- Founded in ```utils/Constant.java```
```
    public static final String API_KEY="Your API Key";
    public static final String UNITS="Untis For Data";
```


[APK Example](https://drive.google.com/file/d/1K2-tA1rZr2fvE-EuvnaTOPL1s6K_uzkN/view?usp=sharing)