from PIL import Image
img = Image.open('/home/dani/Petal/Logo white BG pequeño.jpg')
img = img.resize((512, 512), Image.LANCZOS)
img.save('/home/dani/Petal/icon_512x512.png')
print('Listo')
