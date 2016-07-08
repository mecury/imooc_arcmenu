自定义的的一个卫星导航的button

属性值：
```
<?xml version="1.0" encoding="utf-8"?>
<resources>
    <attr name="position">
        <enum name="left_top" value="0"/>
        <enum name="left_bottom" value="1"/>
        <enum name="right_top" value="2"/>
        <enum name="right_bottom" value="3"/>
    </attr>

    <attr name="radius" format="dimension"/>
    
    <declare-styleable name="ArcMenu">
        <attr name="position"/>
        <attr name="radius"/>
    </declare-styleable>
</resources>
```
演示动画：
![GIF.gif](https://github.com/mecury/imooc_arcmenu/blob/master/Previews/GIF.gif)
