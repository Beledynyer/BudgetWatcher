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
        // Food and Groceries
        addTagRule("(?i)groceries|supermarket|food|spar|woolworths|pick n pay|checkers|steers", "food");
        addTagRule("(?i)restaurant|cafe|coffee|steers|mcdonalds|kfc|nandos", "restaurant");
        addTagRule("(?i)takeaway|delivery|uber eats|mr d|order in", "delivery");

        // Transport
        addTagRule("(?i)uber|taxi|transport|bus|train|flight|airline", "transport");
        addTagRule("(?i)fuel|petrol|gas|engen|shell|caltex", "fuel");

        // Shopping
        addTagRule("(?i)clothing|fashion|woolworths|h&m|zara|cotton on", "clothing");
        addTagRule("(?i)electronics|takealot|incredible connection|game", "electronics");
        addTagRule("(?i)pharmacy|health|clicks|dis-chem", "health");

        // Income
        addTagRule("(?i)deposit|salary|wages|payment received", "income");
        addTagRule("(?i)refund|cashback|reimbursement", "refund");

        // Bills and Utilities
        addTagRule("(?i)electricity|water|rates|municipality", "utilities");
        addTagRule("(?i)rent|mortgage|bond payment", "housing");
        addTagRule("(?i)insurance|medical aid|life cover", "insurance");

        // Education
        addTagRule("(?i)school fees|tuition|textbooks|stationery", "education");

        // Entertainment
        addTagRule("(?i)movies|cinema|theater|concert", "entertainment");
        addTagRule("(?i)gym|fitness|sports", "fitness");

        // Subscriptions
        addTagRule("(?i)netflix|showmax|apple tv|spotify", "entertainment");
        addTagRule("(?i)cell c|vodacom|mtn|telkom", "communication");

        // General tags
        addTagRule("(?i)purchase|buy|pay", "expense");
        addTagRule("(?i)withdrawal|atm", "cash");
        addTagRule("(?i)transfer|eft", "transfer");
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