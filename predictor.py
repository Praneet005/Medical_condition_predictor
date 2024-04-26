import tkinter as tk
from tkinter import ttk
from tkinter import messagebox
import pandas as pd

def get_unique_symptoms(file_path):
    df = pd.read_csv(file_path)
    symptoms = set()
    for col in df.columns:
        if col.startswith('Symptom_'):
            symptoms.update(df[col].dropna())
    return sorted(list(symptoms))

def find_matching_diseases(selected_symptoms):
    matching_diseases = set()
    for idx, row in df.iterrows():
        symptoms = [row[col] for col in df.columns if col.startswith('Symptom_')]
        if set(selected_symptoms).issubset(symptoms):
            matching_diseases.add(row['Disease'])
    return list(matching_diseases)

def filter_symptoms():
    search_text = search_var.get().lower()
    if not search_text:
        update_listbox(all_symptoms)
    else:
        filtered_symptoms = [symptom for symptom in all_symptoms if search_text in symptom.lower()]
        update_listbox(filtered_symptoms)

def update_listbox(symptoms):
    symptom_listbox.delete(0, tk.END)
    for symptom in symptoms:
        symptom_listbox.insert(tk.END, symptom)

def predict_disease():
    selected_indices = symptom_listbox.curselection()
    if not selected_indices:
        messagebox.showwarning("Warning", "Please select at least one symptom.")
        return
    selected_symptoms = [symptom_listbox.get(idx) for idx in selected_indices]
    selected_diseases = find_matching_diseases(selected_symptoms)
    messagebox.showinfo("Predicted Disease", f"The disease could be:\n{'\n '.join(selected_diseases)}")

file_path = "/Users/vishalkannan/Downloads/archive (4) 2/dataset.csv"
df = pd.read_csv(file_path)
all_symptoms = get_unique_symptoms(file_path)


root = tk.Tk()
root.title("Symptom Selector")
root.geometry("500x400")  # Set window size

search_var = tk.StringVar()
search_entry = ttk.Entry(root, textvariable=search_var, width=30)
search_entry.grid(row=0, column=0, padx=10, pady=10)
search_button = ttk.Button(root, text="Search", command=filter_symptoms)
search_button.grid(row=0, column=1, padx=5, pady=10)

symptom_listbox = tk.Listbox(root, selectmode=tk.MULTIPLE, height=15, width=50)  # Increased width
update_listbox(all_symptoms)
symptom_listbox.grid(row=1, column=0, columnspan=2, padx=10, pady=5)

scrollbar = tk.Scrollbar(root, orient="vertical", command=symptom_listbox.yview)
scrollbar.grid(row=1, column=2, sticky="ns")
symptom_listbox.config(yscrollcommand=scrollbar.set)

predict_button = ttk.Button(root, text="Predict", command=predict_disease)
predict_button.grid(row=2, column=0, columnspan=2, padx=10, pady=10)

root.mainloop()

