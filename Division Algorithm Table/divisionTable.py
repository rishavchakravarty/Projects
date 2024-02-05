from reportlab.lib.pagesizes import letter
from reportlab.pdfgen import canvas
from reportlab.lib.styles import getSampleStyleSheet
from reportlab.platypus import Paragraph, Spacer, SimpleDocTemplate, PageBreak, Table, TableStyle
from reportlab.lib.units import inch
from reportlab.lib import colors

def add_custom_title_page(canvas, doc):
    canvas.saveState()
    canvas.setFont('Times-Bold', 16)
    canvas.drawCentredString(letter[0]/2.0, letter[1]-108, "Project-1: Division Table Algorithm")
    canvas.setFont('Times-Roman', 14)
    canvas.drawCentredString(letter[0]/2.0, letter[1]-128, "Prepared by: Kinjal Pandey")
    canvas.restoreState()

def extended_gcd(a, b):
    table = [["Quotient", "Remainder", "X", "Y"]]  # Add headers to the table
    x0, x1 = 1, 0
    y0, y1 = 0, 1
    while b != 0:
        q = a // b
        a, b = b, a % b
        x0, x1 = x1, x0 - q * x1
        y0, y1 = y1, y0 - q * y1
        table.append([q, a, x1, y1])
    return a, x0, y0, table

def generate_pdf_content(a, b, gcd, x, y, table, styles):
    content = [Paragraph(f"<b>Extended Euclidean Algorithm Steps for a = {a}, b = {b}</b>", styles['Heading2']), Spacer(1, 0.2 * inch)]
    t = Table(table)
    t.setStyle(TableStyle([
        ('BACKGROUND', (0,0), (-1,0), colors.grey),
        ('ALIGN', (0,0), (-1,-1), 'CENTER'),
        ('GRID', (0,0), (-1,-1), 1, colors.black),
        ('LEFTPADDING', (0,0), (-1,-1), 3),
        ('RIGHTPADDING', (0,0), (-1,-1), 3),
    ]))
    content.append(t)
    result = Paragraph(f"<b>Final Result:</b> gcd({a}, {b}) = {gcd}, x = {x}, y = {y}", styles['BodyText'])
    content.append(result)
    content.append(Spacer(1, 0.2 * inch))
    return content

def create_pdf(output_filename, all_content):
    doc = SimpleDocTemplate(output_filename, pagesize=letter)
    Story = [Spacer(1, 2 * inch)]  # Spacer at the start
    styles = getSampleStyleSheet()

    for content in all_content:
        Story.extend(content)
        Story.append(PageBreak())

    doc.build(Story, onFirstPage=add_custom_title_page, onLaterPages=add_custom_title_page)

# Main execution
pairs = [(384168, 39096), (494752, 296864), (17601969, 2364768)]
output_filename = "gcd_output.pdf"
all_content = []
styles = getSampleStyleSheet()

for a, b in pairs:
    gcd, x, y, table = extended_gcd(a, b)
    content = generate_pdf_content(a, b, gcd, x, y, table, styles)
    all_content.append(content)

create_pdf(output_filename, all_content)
print("Output generated in gcd_output.pdf")
