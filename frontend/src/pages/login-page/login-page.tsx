import {useForm, SubmitHandler} from 'react-hook-form';
import {zodResolver} from '@hookform/resolvers/zod';
import {z} from 'zod';
import {Button} from "@/components/ui/button/button.tsx";
import {
    Card,
    CardContent,
    CardDescription,
    CardFooter,
    CardHeader,
    CardTitle,
} from "@/components/ui/card";
import {Input} from "@/components/ui/input";
import {Label} from "@/components/ui/label";
import {
    Tabs,
    TabsContent,
    TabsList,
    TabsTrigger,
} from "@/components/ui/tabs";
import {ErrorMessage} from "@/pages/login-page/error-message";
import {useCheckAuthStatus} from "@/hooks/use-login-status";
import {SkeletonDemo} from "@/components/loading";
import {Navigate} from "react-router-dom";

// Zod schemas for form validation
const loginSchema = z.object({
    email: z.string().email('Invalid email address').min(1, 'This field is required!'),
    password: z.string().min(6, 'Password must be at least 6 characters long').max(100, 'Password cannot be more than 100 characters long').min(1, 'This field is required!'),
});


const registerSchema = z.object({
    email: z.string().email('Invalid email address').min(1, 'This field is required!'),
    username: z.string().min(4, 'Username must be at least 4 characters long').min(1, 'This field is required!'),
    password: z.string().min(6, 'Password must be at least 6 characters long').max(100, 'Password cannot be more than 100 characters long').min(1, 'This field is required!'),
    repeatPassword: z.string().min(1, 'This field is required!')
}).refine((data) => data.password === data.repeatPassword, {
    message: "Passwords don't match",
    path: ["repeatPassword"],
});

// Extract types from Zod schemas
type LoginFormFields = z.infer<typeof loginSchema>;
type RegisterFormFields = z.infer<typeof registerSchema>;

export function LoginPage() {
    const userIsLogged = useCheckAuthStatus();

    // LoginForm setup
    const {
        register: registerLogin,
        handleSubmit: handleSubmitLogin,
        formState: {errors: errorsLogin}
    } = useForm<LoginFormFields>({
        resolver: zodResolver(loginSchema),
    });

    // RegisterForm setup
    const {
        register: registerRegister,
        handleSubmit: handleSubmitRegister,
        formState: {errors: errorsRegister},
    } = useForm<RegisterFormFields>({
        resolver: zodResolver(registerSchema),
    });

    // Handlers
    const onSubmitLogin: SubmitHandler<LoginFormFields> = data => console.log(data);
    const onSubmitRegister: SubmitHandler<RegisterFormFields> = data => console.log(data);

    if (userIsLogged) {
        return <Navigate to="/dashboard" replace/>
    }

    if (userIsLogged === null) {
        return <SkeletonDemo/>;
    }
    return (
        <div className="h-screen flex items-center justify-center">
            <Tabs defaultValue="login" className="w-[400px] m-5">
                <TabsList className="grid w-full grid-cols-2 ">
                    <TabsTrigger value="login">Login</TabsTrigger>
                    <TabsTrigger value="register">Register</TabsTrigger>
                </TabsList>
                <div className="min-h-[31rem] pt-5">
                    <TabsContent value="login">
                        <Card>
                            <form onSubmit={handleSubmitLogin(onSubmitLogin)} noValidate>
                                <CardHeader>
                                    <CardTitle>Login</CardTitle>
                                    <CardDescription>
                                        Log in to the system
                                    </CardDescription>
                                </CardHeader>
                                <CardContent className="space-y-2">
                                    <div className="space-y-1">
                                        <Label htmlFor="email">Email</Label>
                                        <Input id="email" type="email" placeholder="m@example.com"
                                               {...registerLogin('email')}
                                        />
                                        <ErrorMessage error={errorsLogin.email}/>
                                    </div>
                                    <div className="space-y-1">
                                        <Label htmlFor="username">Password</Label>
                                        <Input id="username" type="password"
                                               {...registerLogin('password')}
                                        />
                                        <ErrorMessage error={errorsLogin.password}/>
                                    </div>
                                </CardContent>
                                <CardFooter>
                                    <Button>Login</Button>
                                </CardFooter>
                            </form>
                        </Card>
                    </TabsContent>
                    <TabsContent value="register">
                        <form onSubmit={handleSubmitRegister(onSubmitRegister)} noValidate>
                            <Card>
                                <CardHeader>
                                    <CardTitle>Register</CardTitle>
                                    <CardDescription>
                                        Create an account in the system
                                    </CardDescription>
                                </CardHeader>
                                <CardContent className="space-y-2">
                                    <div className="space-y-1">
                                        <Label htmlFor="registerEmail">Email</Label>
                                        <Input id="registerEmail" type="email" placeholder="m@example.com"
                                               {...registerRegister('email')}
                                        />
                                        <ErrorMessage error={errorsRegister.email}/>
                                    </div>
                                    <div className="space-y-1">
                                        <Label htmlFor="registerUsername">Username</Label>
                                        <Input id="registerUsername" type="text" placeholder="Your Name"
                                               {...registerRegister('username')}
                                        />
                                        <ErrorMessage error={errorsRegister.username}/>
                                    </div>
                                    <div className="space-y-1">
                                        <Label htmlFor="registerPassword">Password</Label>
                                        <Input id="registerPassword" type="password"
                                               {...registerRegister('password')}
                                        />
                                        <ErrorMessage error={errorsRegister.password}/>
                                    </div>
                                    <div className="space-y-1">
                                        <Label htmlFor="repeatPassword">Repeat Password</Label>
                                        <Input id="repeatPassword" type="password"
                                               {...registerRegister("repeatPassword")}
                                        />
                                        <ErrorMessage error={errorsRegister.repeatPassword}/>
                                    </div>
                                </CardContent>
                                <CardFooter>
                                    <Button type="submit">Register</Button>
                                </CardFooter>
                            </Card>
                        </form>
                    </TabsContent>
                </div>
            </Tabs>
        </div>
    )
}
