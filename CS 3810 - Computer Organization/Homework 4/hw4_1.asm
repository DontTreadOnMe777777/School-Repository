# This program is Question 1 of Homework 4, written by Brandon Walters. It allows for 64 bit addition, taking 4 numbers from the user as parameters.

.data
str1: .asciiz "Enter 4 numbers:"
str2: .asciiz "The sum of (A * B) + (C * D) is now stored in $t8 and $t9!"

.text
# Print to the user, to prompt them for numbers
li $v0, 4
la $a0, str1
syscall

# Take each of the 4 numbers and store them in $t1 - $t3
li $v0, 5
syscall
addu $t0, $v0, $zero

li $v0, 5
syscall
addu $t1, $v0, $zero

li $v0, 5
syscall
addu $t2, $v0, $zero

li $v0, 5
syscall
addu $t3, $v0, $zero

# Multiply the A and B, and C and D numbers together. The products will be represented by $t4:$t5 and $t6:$t7.
mult $t0, $t1

mfhi $t4
mflo $t5

mult $t2, $t3

mfhi $t6
mflo $t7

# Adds the two sets of lower bits together. This is where the carry may occur.
addu $t9, $t5, $t7

# Checks for a possible carry. If so, we will add it to the higher bits later.
sltu $s0, $t9, $t7

# Adds the carry, if it exists, to the first set of higher bits. This is the manual calculation needed for 64 bit addition.
addu $t8, $s0, $t4

# Adds the two sets of higher bits together.
addu $t8, $t8, $t6

# Tells the user that the final results are stored in registers $t8 and $t9.
li $v0, 4
la $a0, str2
syscall
