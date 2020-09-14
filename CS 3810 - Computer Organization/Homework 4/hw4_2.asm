# This program is Problem 2 of Homework 4, as written by Brandon Walters. The program takes in a string of 39 or less characters given by the user, and returns a compressed version,
# where adjacent equivalent characters are represented by numbers. 

# This program contains two procedure calls, with proper saves and restores.

.data
str1: .asciiz "Please enter less than 40 characters: "
str2: .asciiz "The string is not valid! Please try again."
str3: .asciiz "The compressed form of this string is: "
str4: .space 40 # The buffer we will build the compressed version with.
userInput: .space 40 # The buffer the user will fill.

.text

main:
# Prompt the user to enter a string
li $v0, 4
la $a0, str1
syscall

# Read the string into our buffer.
li $v0, 8
la $a0, userInput
li $a1, 40
syscall

# Save this buffer
move $s0, $a0

# First procedure call, check if each character is a letter.
jal letterCheck

# Second procedure call, compress the string and return the result to the user.
jal compression

# Returns the compressed version of the string to the user.
li $v0, 4
la $a0, str3
syscall

li $v0, 4
la $a0, ($v1)
syscall

# End of program
li $v0, 10
syscall



# Portion to compress the input string
#
# Second procedure call
#
compression:
# Saves values on the stack
addi $sp, $sp, -4
sw $s0, 0($sp)
li $t2, 10 # newline char for check
la $t4, str4 # The buffer we will build with

L2:
lb $t0, ($s0)
# If the character is a newline or empty, the string ends.
beq $t0, $t2, endCompression
beq $t0, $zero, endCompression

# Stores the character in the new string.
sb $t0, 0($t4)

# Increments the pointer
addi $s0, $s0, 1
# Checks the next character. If equal, we will enter a new loop to count the repeating characters.
lb $t1, ($s0)
beq $t0, $t1, equalLetters
# Continues the loop
j L2

equalLetters:
# Since there must be at least two letters next to each other, the count starts at 2.
addi $t3, $zero, 2 

lb $t0, 0($s0)
# If the character is a newline or empty, the string ends.
beq $t0, $t2, endEquals
beq $t0, $zero, endEquals

# Increments the pointer
addi $s0, $s0, 1
# Checks the next character. If not equal, we can stop counting.
lb $t1, 0($s0)
bne $t0, $t1, endEquals

# Increments the count
addi $t3, $t3, 1
# Continues the loop
j equalLetters

# Adds the count to the string, increments the pointer, and returns to the parent loop
endEquals:
sb $t3, 0($t4)
addi $s0, $s0, 1
j L2

# Restores the procedure, saves the new string, returns to the caller
endCompression:
lw $s0, 0($sp)
addi $sp, $sp, 4
add $v1, $t4, $zero
jr $ra


# Portion to check if each character in the input string is a letter
#
# First procedure call
#
letterCheck:
# Saves values on the stack
addi $sp, $sp, -4
sw $s0, 0($sp)
li $t2, 10 #newline char for check

L1:
lb $t0, 0($s0)
# If the character is a newline or empty, the string ends.
beq $t0, $t2, endLetterCheck 
beq $t0, $zero, endLetterCheck 
# Checks the character. If less than A, not a letter, throws error and ends program. Otherwise, continues.
li $t1, 'A'
blt $t0, $t1, notLetter
# If greater than Z, check for lowercase letters.
li $t1, 'Z'
bgt $t0, $t1, checkLower
# If character is a capital letter, increments to the next character.
j increment

# Checks lowercase ASCII values. If not in the range, not a letter, throws error and ends program. Otherwise, increments to the next character.
checkLower:
li $t1, 'a'
blt $t0, $t1, notLetter
li $t1, 'z'
bgt $t0, $t1, notLetter

# Increments the pointer to the next character of the string
increment:
addi $s0, $s0, 1
j L1

# If any character is not a letter, tells the user of the error and ends the program.
notLetter:
li $v0, 4
la $a0, str2
syscall

# End of program
li $v0, 10
syscall

# Restores the procedure, returns to the caller
endLetterCheck:
lw $s0, 0($sp)
addi $sp, $sp, 4
jr $ra

# End letter check portion
#
#
#