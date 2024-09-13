from reportlab.lib.pagesizes import letter
from reportlab.pdfgen import canvas
from reportlab.lib.styles import getSampleStyleSheet
from reportlab.platypus import Paragraph, Spacer, SimpleDocTemplate, PageBreak, Table, TableStyle
from reportlab.lib.units import inch
from reportlab.lib import colors

# Function to add a custom title to the PDF pages
# This function is designed to modify its behavior for the first page
def add_custom_title_page(canvas, doc):
    canvas.saveState()  # Save the current state of the canvas to restore later
    canvas.setFont('Times-Bold', 16)  # Set font for the title
    # Draw the project title at the top of the page
    canvas.drawCentredString(letter[0]/2.0, letter[1]-108, "Project-1: Division Table Algorithm")
    if doc.page == 1:  # Check if it's the first page to include the prepared by line
        canvas.setFont('Times-Roman', 14)  # Set font for the authors
        # Draw the "Prepared by" line only on the first page
        canvas.drawCentredString(letter[0]/2.0, letter[1]-128, "Prepared by: Kinjal Pandey, Daniella Efrach, Mallika Gupta, Kritika Partha")
    canvas.restoreState()  # Restore the saved state

# Function to perform the extended Euclidean algorithm
def extended_gcd(a, b):
    table = [["Quotient", "Remainder", "X", "Y"]]  # Initialize the table with headers
    x0, x1 = 1, 0  # Initial values for x
    y0, y1 = 0, 1  # Initial values for y
    while b != 0:
        q = a // b  # Calculate quotient
        a, b = b, a % b  # Update a and b using Euclid's algorithm
        x0, x1 = x1, x0 - q * x1  # Update x values
        y0, y1 = y1, y0 - q * y1  # Update y values
        table.append([q, a, x1, y1])  # Append the current step to the table
    return a, x0, y0, table  # Return the GCD and coefficients along with the table

# Function to generate the content for the PDF document
def generate_pdf_content(a, b, gcd, x, y, table, styles):
    # Heading for the section
    content = [Paragraph(f"<b>Extended Euclidean Algorithm Steps for a = {a}, b = {b}</b>", styles['Heading2']), Spacer(1, 0.2 * inch)]
    # Create a table with the algorithm steps
    t = Table(table)
    t.setStyle(TableStyle([
        ('BACKGROUND', (0,0), (-1,0), colors.grey),
        ('ALIGN', (0,0), (-1,-1), 'CENTER'),
        ('GRID', (0,0), (-1,-1), 1, colors.black),
        ('LEFTPADDING', (0,0), (-1,-1), 3),
        ('RIGHTPADDING', (0,0), (-1,-1), 3),
    ]))
    content.append(t)  # Add the table to the content
    # Add the final result as a paragraph
    result = Paragraph(f"<b>Final Result:</b> gcd({a}, {b}) = {gcd}, x = {x}, y = {y}", styles['BodyText'])
    content.append(result)  # Add the result to the content
    content.append(Spacer(1, 0.2 * inch))  # Add a spacer for layout
    return content  # Return the assembled content

# Function to create the PDF document
def create_pdf(output_filename, all_content):
    doc = SimpleDocTemplate(output_filename, pagesize=letter)  # Set up the document
    Story = [Spacer(1, 2 * inch)]  # Start with a spacer
    styles = getSampleStyleSheet()  # Get default styles

    # Loop through all content blocks, adding them to the story
    for content in all_content:
        Story.extend(content)  # Add the content block
        Story.append(PageBreak())  # Add a page break after each content block

    # Build the document with custom callbacks for the first and later pages
    doc.build(Story, onFirstPage=add_custom_title_page, onLaterPages=add_custom_title_page)

# Main execution block
if __name__ == "__main__":
    pairs = [(384168, 39096), (494752, 296864), (17601969, 2364768)]  # Define pairs for which to calculate GCD
    output_filename = "gcd_output.pdf"  # Define the output filename
    all_content = []  # Initialize a list to hold content blocks
    styles = getSampleStyleSheet()  # Get default styles

    # Loop through each pair, calculate GCD, and generate content
    for a, b in pairs:
        gcd, x, y, table = extended_gcd(a, b)  # Perform the extended Euclidean algorithm
        content = generate_pdf_content(a, b, gcd, x, y, table, styles)  # Generate content for this pair
        all_content.append(content)  # Add the content to the list

    create_pdf(output_filename, all_content)  # Create the PDF document
    print("Output generated in gcd_output.pdf")
