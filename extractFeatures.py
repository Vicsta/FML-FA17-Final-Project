import json
import time
import re
import string
import enchant


d = enchant.Dict("en_US")
stop_words = {'ourselves', 'hers', 'between', 'yourself', 'but', 'again', 'there', 'about', 'once', 'during', 'out', 'very', 'having', 'with', 'they', 'own', 'an', 'be', 'some', 'for', 'do', 'its', 'yours', 'such', 'into', 'of', 'most', 'itself', 'other', 'off', 'is', 's', 'am', 'or', 'who', 'as', 'from', 'him', 'each', 'the', 'themselves', 'until', 'below', 'are', 'we', 'these', 'your', 'his', 'through', 'don', 'nor', 'me', 'were', 'her', 'more', 'himself', 'this', 'down', 'should', 'our', 'their', 'while', 'above', 'both', 'up', 'to', 'ours', 'had', 'she', 'all', 'no', 'when', 'at', 'any', 'before', 'them', 'same', 'and', 'been', 'have', 'in', 'will', 'on', 'does', 'yourselves', 'then', 'that', 'because', 'what', 'over', 'why', 'so', 'can', 'did', 'not', 'now', 'under', 'he', 'you', 'herself', 'has', 'just', 'where', 'too', 'only', 'myself', 'which', 'those', 'i', 'after', 'few', 'whom', 't', 'being', 'if', 'theirs', 'my', 'against', 'a', 'by', 'doing', 'it', 'how', 'further', 'was', 'here', 'than'}

measure_words = set()
with open('data/measure_words') as f:
    data = f.read().split()
    for l in data:
        measure_words.add(l)

ingredients = set()

translator = str.maketrans('', '', string.punctuation)


def clean(ingredient):
    cleaned = []

    ingredient = ingredient.translate(translator)

    for word in ingredient.lower().split(" "):
        if word in measure_words:
            continue

        if word in stop_words:
            continue

        if word.isnumeric() or re.search("\d/\d", word) or re.search("\d-*.", word):
            continue

        if len(word) == 0 or word.isspace():
            continue

        if d.check(word):
            cleaned.append(word)

    return " ".join(cleaned).strip()


def main():

    debug = False

    start = time.time()
    with open('data/full_format_recipes.json') as f:
        data = json.load(f)

    print("Finished loading", len(data), "recipes", "in", (time.time() - start), "seconds")

    start = time.time()

    errs = 0

    for recipe in data:
        if 'ingredients' in recipe and 'rating' in recipe:
            if debug:
                print("Ingredients:", recipe['ingredients'])
            for ingredient in recipe['ingredients']:
                cleaned_ingredient = clean(ingredient)
                if len(cleaned_ingredient) > 0 and cleaned_ingredient not in ingredients:
                    ingredients.add(cleaned_ingredient)
            if debug:
                print("Rating:", recipe['rating'])
        else:
            errs += 1
            if debug:
                print("Error reading recipe:", recipe)

    print("Finished analyzing", len(data), "recipes", "in", (time.time() - start), "seconds with", errs, "errors")
    print("Number of unique ingredients found:", len(ingredients))

    start = time.time()
    new_ingredients = set()
    for ingredient in ingredients:
        if len(ingredient.split(" ")) == 1:
            new_ingredients.add(ingredient)
    print("Found", len(new_ingredients), "one-word ingredients in", time.time() - start, "seconds")

    # start = time.time()
    # for ingredient in ingredients:
    #     found = False
    #     for word in ingredient.split(" "):
    #         if word in new_ingredients:
    #             found = True
    #             break
    #     if not found and len(ingredient.split(" ")) <= 5 and "optional" not in ingredient:
    #         new_ingredients.add(ingredient)
    # print("Number of unique ingredients found:", len(new_ingredients), "in", time.time() - start, "seconds")

    with open('data/ingredients', 'w') as f:
        for i in new_ingredients:
            f.write(i + "\n")


if __name__ == "__main__":
    main()
