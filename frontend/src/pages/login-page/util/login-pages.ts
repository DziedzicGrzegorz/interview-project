import {z} from "zod";

export const loginSchema = z.object({
    email: z.string().email('Invalid email address').min(1, 'This field is required!'),
    password: z.string().min(6, 'Password must be at least 6 characters long').max(100, 'Password cannot be more than 100 characters long').min(1, 'This field is required!'),
});


export const registerSchema = z.object({
    email: z.string()
        .email('Invalid email address')
        .min(1, 'The email is required.'),
    username: z.string()
        .regex(/^[a-zA-Z0-9]{3,}$/, 'Username must be at least 3 characters long.')
        .min(1, 'The username is required.'),
    password: z.string()
        .regex(/^(?=.*\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=!*()]).{8,100}$/, 'Password must be 8 to 100 characters long and include a combination of uppercase letters, lowercase letters, numbers, and special characters.')
        .min(1, 'The password is required.'),
    repeatPassword: z.string()
        .min(1, 'The repeat password is required.'),
}).refine((data) => data.password === data.repeatPassword, {
    message: "Passwords don't match",
    path: ["repeatPassword"],
});