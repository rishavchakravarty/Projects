from reportlab.lib.pagesizes import letter
from reportlab.pdfgen import canvas
from reportlab.lib.styles import getSampleStyleSheet
from reportlab.platypus import Paragraph, Spacer, SimpleDocTemplate, PageBreak, Table, TableStyle
from reportlab.lib.units import inch
from reportlab.lib import colors
from string import ascii_uppercase
from collections import Counter
import numpy as np
import string

# Cipher Text
cipher_text = """LAVHEBSJMDINFGXCLWTUUWARWBQWFTEHWUDDTCAAKKTXTSMYALMVHTAHJHKICFAFZKLEXA
TXIXYMFVLVGUDALRFJTTGKXNLOYOWLVVVQAFKVGEZLEHEXHEZGPVZEDOWARZEAFSGVASOS
DXJIEZESBEWTDWSFGVOPMUMJENPKWKMMCQKVXJMGZWVBEEWMQLARXGUNWLLWEDKKHCICAF
LKFPOHWJTTGEEKLHKLEUJVTKEAESJXJYLFDSPVRFAJUXDINFAKLFQEFAEXJYNMTDXKSRQU
GOVVTTWUHEXEZLGYVPEOLJHEMCOGEFLRIOSLBFRSRJGFKLEFWUAESLAYQIISVUVWKVZEZA
FKVWPAFKXKSAOGMKKSRPWJHIHUXQSNKLODARXUAADJSGKMSEMWWSCARWVXIELVMVZVJODW
PTDTLQESGPGOYEMGZGAFAGGJWEDNAVVWNAOWGTVYBLUXIXAUFUHDQUZAUTKMOZKTRUIFMM
DMNMTTLZXBIYZWUXJWADQLHUICDQHMKLEOGEFLRIOSLBFRSEGDXCCIZLZXYENPKGYKLEQF
VNJIRFZALRTPXAWLSSTTOZXEXHQVSMRMSUFEHKMOZGNXIILQULKFRIOFWMNSRWKGKRXRQK
LHEENQDWVKVOZAUWVZIOWAYKLEOGEFLRIOSLBFRSBJGOZHEDAKLVVVQVOBKLAISJKRRTEW
WDZRGFZGLVGOYEMGZGAFAGGJXHQHJHMMDQJUTEROFHJHMMDQLZXUETMTWVRYSQALARWDQK
AZEIDFZWMVGHZGDHXCSGUZMYETULUTEROFTWTTGEEKWWSCAZQLAZVDBSJMPAEPGFHKLAHW
SGPWIXNWKSYLXWLLRRDFZWWZWCGKKBFRSIALAZRTTWWQVGUFANXSVAZUZTIISFADEFRGAA
FZNLIXWLAVVETSKGFXYQLTXVRAPWUBJMOZOZXKLEDLGLVIKXWYBJPAFAGGNIMGKLPFVKIA
LATSNSJWLJMNPMKMICAOSVXDMCEHJBMECKYJHLTSMFVHKLEDKLHTVARLSGRTPDGSVYXHML
SWUVEEKWLRPLAXLAVQUXLAICICAEHXKMNSUGGTIRZKLARXHMNWUVINFZWYFGUEGXLFQUOZ
VXSETQTMMNICMFSECEGDWWMYETIWOBCPNQWVHEKOUFYAFREELSGUMNRGJFVHPGTDBTHENS
LXRFOGLZHNFEELLHGVOFWUMCMBQJLRRRDEWUNIMTKAFUFXHAMJERASMFVHLVTQUZGFPOSQ"""

cipher_text = cipher_text.replace("\n", "").replace(" ", "")

def index_of_coincidence(cipher_text, m):
    """
    Calculate the index of coincidence for each substring of the cipher text
    for a given keyword length m.

    Parameters:
    - cipher_text: str. The cipher text from which to calculate the IC.
    - m: int. The guessed keyword length.

    Returns:
    A list containing the index of coincidence for each substring.
    """
    # Split the cipher text into m substrings
    substrings = ['' for _ in range(m)]
    for i, char in enumerate(cipher_text):
        substrings[i % m] += char

    # Function to calculate the IC of a single substring
    def calculate_ic(substring):
        N = len(substring)
        frequency = Counter(substring)
        ic = sum(f*(f-1) for f in frequency.values()) / (N*(N-1))
        return ic

    # Calculate the IC for each substring
    ics = [calculate_ic(substring) for substring in substrings]
    return ics

# Calculate the index of coincidences for m = 6, 7, 8
m_values = [6, 7, 8]
ics_results = {m: index_of_coincidence(cipher_text, m) for m in m_values}

#------------------PART 2--------------------------------------------------

# Splitting the cipher text into 7 substrings
substrings = ['' for _ in range(7)]
for i, char in enumerate(cipher_text):
    substrings[i % 7] += char

# Function to calculate the index of coincidence
def index_of_coincidence(text):
    N = len(text)
    freqs = Counter(text)
    ic = sum(freq * (freq - 1) for freq in freqs.values()) / (N * (N - 1))
    return ic

# Calculating the index of coincidence for each substring
ic_values = [index_of_coincidence(substring) for substring in substrings]








#----------------------------------------------------------------------------------------------------
content = []

# Create a table with the algorithm steps
doc = SimpleDocTemplate("Vigenere_Cipher_Report.pdf",ppagesize=letter)

# Styles
styles = getSampleStyleSheet()
styleN = styles['Normal']
styleH = styles['Heading1']

# Title
# Add a title
content.append(Paragraph("Index of Coincidence for Each Substring", styles["Title"]))
content.append(Paragraph("Prepared by Kinjal Pandey, Kritika Partha" ))
content.append(Spacer(1, 0.2 * inch))
# Introduction
intro_text = """This document provides the results of the Index of Coincidence (IC) analysis for the given cipher text with guessed keyword lengths of 6, 7, and 8. The IC values help in estimating the likelihood of a guessed keyword length being correct by comparing the structure of substrings with the expected distribution of letters in English."""
content.append(Paragraph(intro_text, styleN))
content.append(Spacer(1, 12))

# Analysis Results
analysis_results = "The Index of Coincidence (IC) values calculated for each substring with guessed keyword lengths are as follows: "
m6 = "- For m = 6, the IC values are approximately " + ", ".join(str(val) for val in ics_results[6])
m7 = "- For m = 7, the IC values are approximately " + ", ".join(str(val) for val in ics_results[7])
m8 = "- For m = 8, the IC values are approximately " + ", ".join(str(val) for val in ics_results[8])


explaination_ics= "An IC value close to 0.065 suggests that the substring is less random and more structured, resembling the distribution of a standard text in the corresponding language. This indicates that the substrings encrypted with the same key letter (if the guessed keyword length is correct) reveal a structure that resembles the natural frequency of letters in English."

content.append(Paragraph(analysis_results, styleN))
content.append(Spacer(1, 12))
content.append(Paragraph(m6, styleN))
content.append(Spacer(1, 12))
content.append(Paragraph(m7, styleN))
content.append(Spacer(1, 12))
content.append(Paragraph(m8, styleN))
content.append(Spacer(1, 12))
content.append(Paragraph(explaination_ics, styleN))
content.append(Spacer(1, 12))

# Conclusion
conclusion_text = """
Based on the calculated IC values, the analysis supports our guess that m = 7 is the correct keyword length for the ciphered text. This is because the IC values for m = 7 are the highest and most consistent, indicating a significant similarity to the expected distribution of letters in English text, and thus revealing a structure indicative of correct keyword length.
"""
content.append(Paragraph(conclusion_text, styleN))
content.append(Spacer(1, 0.2 * inch))




# Add the substrings and their IC values to the report
for i, (substring, ic) in enumerate(zip(substrings, ic_values), start=1):
    content.append(Paragraph(f"Substring y{i}:", styles["Heading2"]))
    content.append(Paragraph(substring, styles["BodyText"]))
    content.append(Paragraph(f"Index of Coincidence: {ic:.4f}", styles["BodyText"]))
    content.append(Spacer(1, 0.2 * inch))

#--------------------------------------------------------------------------------------


# Given English letter frequencies
p = (0.082, 0.015, 0.018, 0.034, 0.104, 0.020, 0.016, 0.049, 0.063,
     0.003, 0.006, 0.035, 0.025, 0.067, 0.076, 0.020, 0.001, 0.055,
     0.061, 0.093, 0.028, 0.010, 0.023, 0.002, 0.020, 0.001)

# Function to calculate the frequency of each letter in a string
def calculate_frequencies(string):
    N = len(string)
    frequencies = {letter: 0 for letter in ascii_uppercase}
    for char in string.upper():
        if char in frequencies:
            frequencies[char] += 1
    return [frequencies[char]/N for char in ascii_uppercase]

# Function to cyclically shift a list
def cyclic_shift(lst, n):
    return lst[-n:] + lst[:-n]

# Compute dot product of two vectors
def dot_product(v1, v2):
    return sum(x * y for x, y in zip(v1, v2))

y_values = substrings

# Initialize table data with headers
table_data = [["g Value"] + [f"Mg for y{idx+1}" for idx in range(len(y_values))]]

# Initialize a dictionary to store the Mg values for each y substring
mg_values = []

# Calculate Mg values for all possible shifts for each y value
for g in range(26):
    table_row = [g]
    for y in y_values:
        q = calculate_frequencies(y)
        vg = cyclic_shift(q, g)
        Mg = dot_product(p, vg)
        table_row.append(f"{Mg:.3f}")

        # Store the Mg value for this y in the dictionary
        mg_values.append(Mg)

    table_data.append(table_row)

# The table contains all Mg values for each g and y.

# Initialize a list to store the tuples (g value, Mg for y value, number)
mg_values_tuples = []

# We skip the first row as it's the header, hence start from index 1
for row_index, row in enumerate(table_data[1:], start=1):
    g_value = row[0]  # This is the g value for the row
    for col_index, mg_value_str in enumerate(row[1:], start=1):  # Skip the g value itself
        mg_value = float(mg_value_str)  # Convert the Mg value from string to float
        # Create a tuple and add it to the list
        # (g value, Mg for y value, number)
        mg_values_tuples.append((g_value, f"Mg for y{col_index}", mg_value))

# Step 1: Find the top 7 values closest to 0.065
target_value = 0.065
closest_values = sorted(mg_values_tuples, key=lambda x: abs(x[2] - target_value))[:7]




#-------------------------------------------------------------------------------------------------------------
def chi_squared(observed, expected):
    """Calculate the Chi-squared statistic for two lists of observed and expected frequencies."""
    return sum((o - e)**2 / e for o, e in zip(observed, expected) if e)

def decrypt_vigenere_with_chi_squared(cipher_text, key_length):
    # English letter frequencies (source: Cornell University)
    english_freqs = [0.082, 0.015, 0.028, 0.043, 0.127, 0.022, 0.020, 0.061, 0.070, 0.002, 0.008, 0.040, 0.024, 0.067, 0.075, 0.019, 0.001, 0.060, 0.063, 0.091, 0.028, 0.010, 0.023, 0.001, 0.020, 0.001]

    alphabet = 'ABCDEFGHIJKLMNOPQRSTUVWXYZ'
    n = len(alphabet)
    key = ''

    # Split cipher text into key_length substrings
    substrings = ['' for _ in range(key_length)]
    for i, char in enumerate(cipher_text):
        substrings[i % key_length] += char

    # Calculate Chi-squared for each substring to deduce key
    for substring in substrings:
        chi_squared_values = []
        for shift in range(n):
            shifted = ''.join(alphabet[(alphabet.index(c) - shift) % n] for c in substring)
            observed_freqs = [shifted.count(c) / len(shifted) for c in alphabet]
            chi = chi_squared(observed_freqs, english_freqs)
            chi_squared_values.append(chi)
        
        # Find the shift with the minimum Chi-squared value
        min_chi_shift = chi_squared_values.index(min(chi_squared_values))
        key += alphabet[min_chi_shift]
    
    return key


# Deduce the key
key_length = 7  # Assuming we know or correctly guessed the key length is 7
deduced_key = decrypt_vigenere_with_chi_squared(cipher_text, key_length)
#print(f"Deduced Key: {deduced_key}")


def decrypt_vigenere(cipher_text, key):
    """
    Decrypt a Vigenere cipher with a given key.

    Parameters:
    - cipher_text: str. The text to decrypt.
    - key: str. The decryption key.

    Returns:
    - The decrypted text.
    """
    alphabet = 'ABCDEFGHIJKLMNOPQRSTUVWXYZ'
    decrypted_text = ''
    key_length = len(key)
    for i, char in enumerate(cipher_text):
        if char in alphabet:  # Ensure character is in the alphabet
            key_char = key[i % key_length]
            char_index = alphabet.index(char) - alphabet.index(key_char)
            decrypted_text += alphabet[char_index % len(alphabet)]
        else:
            decrypted_text += char
    return decrypted_text




# Create the table
table = Table(table_data, colWidths=[1*inch] * (1 + len(y_values)))

# Add some style to the table
style = TableStyle([
    ('BACKGROUND', (0, 0), (-1, 0), colors.grey),
    ('TEXTCOLOR', (0, 0), (-1, 0), colors.whitesmoke),
    ('ALIGN', (0, 0), (-1, -1), 'CENTER'),
    ('FONTNAME', (0, 0), (-1, 0), 'Helvetica-Bold'),
    ('BOTTOMPADDING', (0, 0), (-1, 0), 12),
    ('BACKGROUND', (0, 1), (-1, -1), colors.beige),
    ('GRID', (0, 0), (-1, -1), 1, colors.black)
])
table.setStyle(style)


content.append(table)
content.append(Spacer(1, 0.2 * inch))

# Decrypting Process Explanation
decrypt_explanation = """
<b>Decrypting Process</b>
<br/>
This section details the process used to decrypt the Vigenère cipher text and deduce the key using the Chi-squared test. The Chi-squared test is applied to each substring generated by dividing the cipher text according to the assumed key length. For each substring, the test is used to compare the observed frequencies of letters (after applying a shift) to the expected frequencies of letters in standard English text. The shift that results in the closest match to the expected frequencies (i.e., the lowest Chi-squared value) indicates the letter of the key for that particular substring. Repeating this process for all substrings yields the complete key.
"""

# Deduced Key
deduced_key_paragraph = f"<b>Deduced Key:</b> The process led to the deduction of the key '{deduced_key}'. This key is used to decrypt the cipher text, suggesting it was encrypted with a Vigenère cipher using this key."

content.append(Paragraph(decrypt_explanation, styleN))
content.append(Spacer(1, 12))
content.append(Paragraph(deduced_key_paragraph, styleN))
content.append(Spacer(1, 0.2 * inch))

# Decrypt the cipher text
decrypted_text = decrypt_vigenere(cipher_text, deduced_key)

# Explanation of the Decryption Process
decryption_explanation = """
<b>Decryption Process</b>
<br/>
After deducing the key, the next step is to decrypt the cipher text. The Vigenère cipher decryption process involves reversing the encryption steps. For each letter in the cipher text, the corresponding letter in the key is used to determine the original letter in the plaintext. This is done by subtracting the position of the key letter from the position of the cipher text letter, then taking the modulo with the alphabet length to handle wrap-around. The result is the original plaintext letter. This process is repeated for each letter in the cipher text to reveal the decrypted text.
"""

# Decrypted Text
decrypted_text_paragraph = f"<b>Decrypted Text:</b> <br/><br/>{decrypted_text}"

content.append(Paragraph(decryption_explanation, styleN))
content.append(Spacer(1, 12))
content.append(Paragraph(decrypted_text_paragraph, styleN))
content.append(Spacer(1, 0.2 * inch))

decrypted_text_paragraph_punctuated = f"<b>Decrypted Text with punctuations:</b> <br/><br/>{decrypted_text}"
decrypted_text_punctuated = "The Department of Justice has been, and will always be, committed to protecting the liberty and security of those whom we serve. In recent months, however, we have on a new scale seen mainstream products and services designed in a way that gives users sole control over access to their data. As a result, law enforcement is sometimes unable to recover the content of electronic communications from the technology provider, even in response to a court order or duly authorized warrant issued by a federal judge. For example, many communications services now encrypt certain communications by default, with the key necessary to decrypt the communications solely in the hands of the end user. This applies both when the data is in motion over electronic networks or at rest on an electronic device. If the communications provider is served with a warrant seeking those communications, the provider cannot provide the data because it has designed the technology such that it cannot be accessed by any third party. We do not have any silver bullets, and the discussions within the executive branch are still ongoing. While there has not yet been a decision whether to seek legislation, we must work with Congress, industry, academics, privacy groups, and others to craft an approach that addresses all of the multiple competing concerns that have been the focus of so much debate. But we can all agree that we will need ongoing, honest, and informed public debate about how best to protect liberty and security in both our laws and our technology."
content.append(Paragraph(decrypted_text_paragraph_punctuated, styleN))
content.append(Spacer(1, 12))
content.append(Paragraph(decrypted_text_punctuated, styleN))
content.append(Spacer(1, 12))
# Build PDF
doc.build(content)
