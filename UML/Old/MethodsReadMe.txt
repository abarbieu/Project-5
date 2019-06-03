getCurrentImage(): functions -> Entity & Background, both have curr image
keep: 
	processImageLine()
	getImages()
	setAlpha()
because they modify PImage inputs or other, not one of our objects
move to Entity() as static:
	all createEntityType
	because its like constructing an entity
ore key and some other attributes are both in WorldModel and Entity because both use them separately

