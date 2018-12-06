package org.upgrad.common;

import com.google.common.base.Charsets;
import com.google.common.hash.Hashing;
import org.upgrad.models.Category;

import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Util {
    public static String hashPassword(String plainPassword){
        if(plainPassword==null || plainPassword.isEmpty())
        {
            return null;
        }
        String sha256hex = Hashing.sha256()
                .hashString(plainPassword, Charsets.US_ASCII)
                .toString();
        return sha256hex;
    }

    public static boolean validateEmailFormat(String email){
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\."+
                "[a-zA-Z0-9_+&*-]+)*@" +
                "(?:[a-zA-Z0-9-]+\\.)+[a-z" +
                "A-Z]{2,7}$";
        Pattern pat = Pattern.compile(emailRegex);
        if (email == null)
            return false;
        return pat.matcher(email).matches();
    }

    public static boolean validateContactNumber(String contactNum){
        Pattern p = Pattern.compile("[0-9]{10}");
        Matcher m = p.matcher(contactNum);
        return m.matches();

    }

    public static boolean validatePassword(String password){
        String pattern = "(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[#@$%&*!^])(?=\\S+$).{8,}";
        return password.matches(pattern);
    }

    public static Comparator<Category> categoryComparator = new Comparator<Category>() {

        public int compare(Category s1, Category s2) {
            String categoryName1 = s1.getCategoryName().toUpperCase();
            String categoryName2 = s2.getCategoryName().toUpperCase();

            //ascending order
            return categoryName1.compareTo(categoryName2);

            //descending order
            //return StudentName2.compareTo(StudentName1);
        }};

    public static String getInStringFormat(List<Category> categories){
        //categories.toArray();
        StringBuilder result = new StringBuilder();
        boolean isFirstIndex = false;
        for(Category category : categories){
            if(!isFirstIndex){
                isFirstIndex = true;
                result.append(category.getCategoryName());
            }else{
                result.append(", " + category.getCategoryName());
            }


        }
        return result.toString();
    }
}
