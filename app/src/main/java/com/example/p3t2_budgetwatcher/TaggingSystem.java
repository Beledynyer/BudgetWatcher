package com.example.p3t2_budgetwatcher;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class TaggingSystem {
    private Map<Pattern, String> tagRules;

    public TaggingSystem() {
        tagRules = new HashMap<>();
        initializeDefaultRules();
    }

    private void initializeDefaultRules() {
        addTagRule("(?i)groceries|supermarket|food", "food");
        addTagRule("(?i)restaurant|cafe|coffee", "dining_out");
        addTagRule("(?i)uber|taxi|transport", "transport");
        addTagRule("(?i)deposit|salary", "income");
        // Add more default rules as needed
    }

    public void addTagRule(String pattern, String tag) {
        tagRules.put(Pattern.compile(pattern), tag);
    }

    public void tagTransaction(Transaction transaction) {
        String description = transaction.getDescription().toLowerCase();
        for (Map.Entry<Pattern, String> rule : tagRules.entrySet()) {
            if (rule.getKey().matcher(description).find()) {
                transaction.addTag(rule.getValue());
            }
        }

        // Add default tags based on transaction type
        if (transaction.getAmount() < 0) {
            transaction.addTag("expense");
        } else {
            transaction.addTag("income");
        }
    }
}