import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class CalculatorModel {
    private List<String> history;

    public CalculatorModel() {
        history = new ArrayList<>();
        loadHistory();
    }

    public String evaluate(String expression) throws ScriptException {
        ScriptEngine engine = new ScriptEngineManager().getEngineByName("JavaScript");
        String modifiedExpression = expression.replace("**", "^");
        Object result = engine.eval(modifiedExpression);
        String resultStr = result.toString();
        history.add(expression + " = " + resultStr);
        return resultStr;
    }

    public List<String> getHistory() {
        return history;
    }

    public void saveHistoryToFile(String path) throws IOException {
        if (path == null || path.isEmpty()) {
            path = "history.log";
        }

        File file = new File(path);
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            for (String entry : history) {
                writer.write(entry);
                writer.newLine();
            }
        }
    }

    private void loadHistory() {
        File file = new File("history.log");
        if (file.exists()) {
            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    history.add(line);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}


import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.io.File;

public class CalculatorView extends JFrame {
    private JTextField inputField;
    private JTextArea historyArea;
    private JButton calculateButton;
    private JButton saveButton;
    private JButton exitButton;

    public CalculatorView() {
        setTitle("Calculator");
        setSize(400, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        inputField = new JTextField(20);
        historyArea = new JTextArea(10, 30);
        historyArea.setEditable(false);

        calculateButton = new JButton("Calculate");
        saveButton = new JButton("Save");
        exitButton = new JButton("Exit");

        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.add(inputField, BorderLayout.NORTH);
        panel.add(new JScrollPane(historyArea), BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(calculateButton);
        buttonPanel.add(saveButton);
        buttonPanel.add(exitButton);

        panel.add(buttonPanel, BorderLayout.SOUTH);
        add(panel);

        setVisible(true);
    }

    public String getInput() {
        return inputField.getText();
    }

    public void setInput(String input) {
        inputField.setText(input);
    }

    public void appendHistory(String entry) {
        historyArea.append(entry + "\n");
    }

    public void addCalculateListener(ActionListener listener) {
        calculateButton.addActionListener(listener);
    }

    public void addSaveListener(ActionListener listener) {
        saveButton.addActionListener(listener);
    }

    public void addExitListener(ActionListener listener) {
        exitButton.addActionListener(listener);
    }

    public File showSaveDialog() {
        JFileChooser fileChooser = new JFileChooser();
        int result = fileChooser.showSaveDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            return fileChooser.getSelectedFile();
        }
        return null;
    }
}
