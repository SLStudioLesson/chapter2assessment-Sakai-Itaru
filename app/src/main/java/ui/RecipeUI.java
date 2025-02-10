package ui;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
import java.util.ArrayList;

import data.RecipeFileHandler;

public class RecipeUI {
    private BufferedReader reader;
    private RecipeFileHandler fileHandler;

    public RecipeUI() {
        reader = new BufferedReader(new InputStreamReader(System.in));
        fileHandler = new RecipeFileHandler();
    }

    public RecipeUI(BufferedReader reader, RecipeFileHandler fileHandler) {
        this.reader = reader;
        this.fileHandler = fileHandler;
    }

    public void displayMenu() {
        while (true) {
            try {
                System.out.println();
                System.out.println("Main Menu:");
                System.out.println("1: Display Recipes");
                System.out.println("2: Add New Recipe");
                System.out.println("3: Search Recipe");
                System.out.println("4: Exit Application");
                System.out.print("Please choose an option: ");

                String choice = reader.readLine();

                switch (choice) {
                    case "1":
                        // 設問1: 一覧表示機能
                        this.displayRecipes();
                        break;
                    case "2":
                        // 設問2: 新規登録機能
                        this.addNewRecipe();
                        break;
                    case "3":
                        // 設問3: 検索機能
                        this.searchRecipe();
                        break;
                    case "4":
                        System.out.println("Exit the application.");
                        return;
                    default:
                        System.out.println("Invalid choice. Please select again.");
                        break;
                }
            } catch (IOException e) {
                System.out.println("Error reading input from user: " + e.getMessage());
            }
        }
    }

    /**
     * 設問1: 一覧表示機能
     * RecipeFileHandlerから読み込んだレシピデータを整形してコンソールに表示します。
     */
    private void displayRecipes() {
        // readRecipes()の戻り値がnullでないなら
        if(fileHandler.readRecipes() != null) {
            System.out.println("Recipes: ");
            System.out.println("-----------------------------------");
            for (String recipeLine : fileHandler.readRecipes()) {
                // Listの要素を,で区切って配列に格納
                String[] recipeParts = recipeLine.split(",");
                if (recipeParts.length > 0) {
                    System.out.println("Recipe Name: " + recipeParts[0]); // 先頭のレシピ名出力
                    System.out.print("Main Ingredients: ");
                    for (int i = 1; i < recipeParts.length; i++) {
                        System.out.print(recipeParts[i]);
                        if (i < recipeParts.length - 1) { // 要素の最後以外には", "を間に挿入
                            System.out.print(", ");
                        }
                    }
                    System.out.println();
                }
            }
                System.out.println("-----------------------------------");
        } else {
            System.out.println("No recipes available.");
        }
    }

    /**
     * 設問2: 新規登録機能
     * ユーザーからレシピ名と主な材料を入力させ、RecipeFileHandlerを使用してrecipes.txtに新しいレシピを追加します。
     *
     * @throws java.io.IOException 入出力が受け付けられない
     */
    private void addNewRecipe() throws IOException {
        // レシピ名入力受付
        System.out.print("Enter recipe name: ");
        String recipeName = this.reader.readLine();
        // 材料入力受付
        System.out.print("Enter main ingredients (comma separated): ");
        String ingredients = this.reader.readLine();
        // 入力された内容を渡し、ファイルに書き込み依頼
        fileHandler.addRecipe(recipeName, ingredients);
        // 書き込み完了で、成功メッセージ
        System.out.println("Recipe added successfully.");
    }

    /**
     * 設問3: 検索機能
     * ユーザーから検索クエリを入力させ、そのクエリに基づいてレシピを検索し、一致するレシピをコンソールに表示します。
     *
     * @throws java.io.IOException 入出力が受け付けられない
     */
    private void searchRecipe() throws IOException {
        System.out.print("Enter search query (e.g., 'name=Tomato&ingredient=Garlic'):");
        String query = this.reader.readLine(); // ユーザーからクエリ入力受付
        String[] queryPairs = query.split("&"); // &で区切りqueryPartsに格納
        ArrayList<String> searchList = new ArrayList<>(); // 検索で一致したものを入れるList

        for (int i = 0; i < queryPairs.length; i++) {
            String[] keyValue = queryPairs[i].split("="); // queryPartsの要素を=で区切りpartsに格納
            for(int j = 0; j < fileHandler.readRecipes().size(); j++) {
                if (fileHandler.readRecipes().get(i).contains(keyValue[i + 1])) {
                    searchList.add(fileHandler.readRecipes().get(i));
                }
            }
        }

        System.out.println("Search Results:");
        // searchListが空じゃなければ、全出力
        if (!searchList.isEmpty()) {
            for (String result : searchList) {
                System.out.println(result);
            }
        } else {
            System.out.println("No recipes found matching the criteria.");
        }
    }
}

