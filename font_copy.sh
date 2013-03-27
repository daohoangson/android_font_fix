# create res/raw directory if not exists
mkdir res/raw

# remove existing ttf files
rm res/raw/*.ttf

# copy new ttf files
cp font_roboto/*.ttf res/raw/.

# rename files to make it compatible with res/raw requirements
rename 's/-/_/' res/raw/*.ttf
rename 'y/A-Z/a-z/' res/raw/*.ttf

