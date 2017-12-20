# How to Train Your Palate: FML-FA17-Final-Project
## Victor Treaba (vgt219) and Herbert Li (hl1785)

This directory contains the code we used to featurize and test our data.
For more details see inline comments and/or our project report.
File and Directory Structure:

Recipes/
    Recipes_RAW/ - Raw data in zipped and JSON formats
    ingredients.txt - ALL ingredients
    ingredients_sparse.txt - SPARSE ingredients
    ingredients_grouped.txt - GROUPED ingredients
    ingredients_grouped_weights.txt - GROUPED WEIGHTS ingredients
    ingredients_sparse_weights.txt - SPARSE WEIGHTS ingredients
    run_algo.py - python code for feature vector creation, running SVM, Adaboost
    CSV Neural.py - python code for running Neural Networks
    Java Code/Main.Java - Code that has the 73 category buckets coded as arrays, 
    	has methods that take the recipe data, and depending on the boolean that is 
    	passed with them, appends directions to the feature vectors, or controls whether 
    	the data distribution is randomly split or split evenly. To change to presence, 
    	frequency, or grouped, the inner code condtions must be changed manually.
    Java Code/Feature.Java - Helper class to store which category an ingredient 
	comes from for converting to grams.
