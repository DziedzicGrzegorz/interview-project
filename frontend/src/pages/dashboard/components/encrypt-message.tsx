import {useForm, SubmitHandler} from 'react-hook-form';
import {zodResolver} from '@hookform/resolvers/zod';
import {z} from 'zod';
import {Button} from "@/components/ui/button/button.tsx";
import {Card, CardContent, CardHeader} from "@/components/ui/card";
import {Dialog, DialogContent, DialogFooter, DialogHeader, DialogTitle, DialogTrigger} from "@/components/ui/dialog";
import {Input} from "@/components/ui/input";
import {Label} from "@/components/ui/label";
import {useToast} from "@/components/ui/use-toast.ts";
import {Axios} from "@/utils/Axios.ts";
import {TextGenerateEffect} from "@/components/text-generator-effect.tsx";
import {useState} from "react";

const encryptRequestSchema = z.object({
    message: z.string().min(1, "Message is required").max(255, "Message must be at most 255 characters"),
    password: z.string().min(6, "Password must be at least 6 characters").max(255, "Encryption password must be at most 255 characters"),
    messageIdentifier: z.string().min(1, "Message identifier is required").max(255, "Message identifier must be at most 255 characters"),
});

interface EncryptMessageProps {
    onFormSubmitSuccess: () => void;
}

type EncryptRequest = z.infer<typeof encryptRequestSchema>;

export function EncryptMessage({onFormSubmitSuccess}: EncryptMessageProps) {
    const {register, handleSubmit, formState: {errors}, reset} = useForm<EncryptRequest>({
        resolver: zodResolver(encryptRequestSchema)
    });
    const {toast} = useToast();
    const [dialogOpen, setDialogOpen] = useState(false);

    const onSubmit: SubmitHandler<EncryptRequest> = async (data) => {
        try {
            await Axios.post('message/encrypt', data)

            toast({
                title: "Success",
                description: "Message encrypted successfully",
            });

            onFormSubmitSuccess()

            await new Promise(resolve => setTimeout(resolve, 1000));

            reset()
            setDialogOpen(false);
        } catch (error) {
            toast({
                variant: "destructive",
                title: "Error",
                description: "Failed to encrypt message",
            });
        }
    };

    return (
        <Dialog open={dialogOpen} onOpenChange={setDialogOpen}>
            <DialogTrigger asChild>
                <button
                    className="inline-flex h-8 xl:h-12  animate-shimmer items-center justify-center rounded-md border border-slate-800 bg-[linear-gradient(110deg,#000103,45%,#1e2631,55%,#000103)] bg-[length:200%_100%] p-6 font-medium text-slate-400 transition-colors focus:outline-none focus:ring-2 focus:ring-slate-400 focus:ring-offset-2 focus:ring-offset-slate-50 hover:text-slate-200">
                    Encrypt Message
                </button>
            </DialogTrigger>
            <DialogContent className="sm:max-w-[425px]">
                <DialogHeader>
                    <DialogTitle>Encrypt Message</DialogTitle>
                </DialogHeader>
                <form onSubmit={handleSubmit(onSubmit)}>
                    <Card>
                        <CardHeader>
                            <h3 className="text-lg font-medium leading-6 text-gray-900">
                                <TextGenerateEffect words={"Encrypt Your Message"}/>
                            </h3>
                        </CardHeader>
                        <CardContent className="space-y-4">
                            <div>
                                <Label htmlFor="message">Message</Label>
                                <Input id="message" type="text" {...register('message')} />
                                {errors.message && <p className="text-red-500">{errors.message.message}</p>}
                            </div>
                            <div>
                                <Label htmlFor="password">Password</Label>
                                <Input id="password" type="password" {...register('password')} />
                                {errors.password && <p className="text-red-500">{errors.password.message}</p>}
                            </div>
                            <div>
                                <Label htmlFor="messageIdentifier">Message Identifier</Label>
                                <Input id="messageIdentifier" type="text" {...register('messageIdentifier')} />
                                {errors.messageIdentifier &&
                                    <p className="text-red-500">{errors.messageIdentifier.message}</p>}
                            </div>
                        </CardContent>
                        <DialogFooter>
                            <Button type="submit">Encrypt & Save</Button>
                        </DialogFooter>
                    </Card>
                </form>
            </DialogContent>
        </Dialog>
    );
}
